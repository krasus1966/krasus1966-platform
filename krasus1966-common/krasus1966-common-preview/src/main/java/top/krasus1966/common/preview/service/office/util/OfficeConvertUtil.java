package top.krasus1966.common.preview.service.office.util;

import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.LocalConverter;
import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.service.IFileService;
import top.krasus1966.common.preview.entity.CommonMultipartFile;
import top.krasus1966.common.preview.entity.FileAttribute;
import top.krasus1966.common.preview.entity.FilePreviewType;
import top.krasus1966.common.preview.entity.ResultMsg;
import top.krasus1966.common.preview.exception.ConvertException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/5/6 23:22
 **/
@Slf4j
public class OfficeConvertUtil {
    private final static String pdf2jpg_image_format = ".jpg";

    /**
     * word转PDF
     *
     * @param filePath   源文件路径
     * @param outputPath 输出文件路径
     * @return ResultMsg
     * @throws OfficeException
     * @method wordConvert2PDF
     * @author krasus1966
     * @date 2023/5/6 23:32
     * @description word转PDF
     */
    public static ResultMsg officeConvert2PDF(String filePath, String outputPath) throws OfficeException {
        if (null == filePath || "".equals(filePath)) {
            return new ResultMsg(ResultMsg.FAILED, "源文件地址不能为空");
        }
        File inputFile = new File(filePath);
        return officeConvert2PDF(inputFile, outputPath);
    }

    /**
     * office文件转换PDF
     *
     * @param source     源文件
     * @param outputPath 目标输出文件
     * @return top.krasus1966.common.preview.entity.ResultMsg
     * @throws OfficeException
     * @method officeConvert2PDF
     * @author krasus1966
     * @date 2023/5/7 02:40
     * @description office文件转换PDF
     */
    public static ResultMsg officeConvert2PDF(File source, String outputFilePath) throws OfficeException {
        if (null == outputFilePath || "".equals(outputFilePath)) {
            return new ResultMsg(ResultMsg.FAILED, "目标文件地址不能为空");
        }
        if (null == source || !source.exists()) {
            return new ResultMsg(ResultMsg.FAILED, "源文件不存在");
        }
        converterFile(source, outputFilePath);
        return new ResultMsg(ResultMsg.SUCCESS);
    }

    /**
     * 文件转换
     *
     * @param inputFile  输入文件
     * @param outputPath 输出文件
     * @return void
     * @throws OfficeException
     * @method converterFile
     * @author krasus1966
     * @date 2023/5/6 23:31
     * @description 文件转换
     */
    public static void converterFile(File inputFile, String outputFilePath) throws OfficeException {
        File outputFile = new File(outputFilePath);
        LocalConverter.Builder builder = LocalConverter.builder();
        builder.build().convert(inputFile).to(outputFile).execute();
    }

    /**
     * 获取预览类型
     *
     * @param fileAttribute 文件属性
     * @param tempFilePath 临时文件地址
     * @param fileService 文件存储Service
     * @return java.util.List<top.krasus1966.common.file.entity.dto.FileInfoDTO>
     * @throws
     * @method getPreviewType
     * @author krasus1966
     * @date 2023/5/7 02:39
     * @description 获取预览类型
     */
    public static List<FileInfoDTO> getPreviewType(FileAttribute fileAttribute,
                                                   String tempFilePath, IFileService fileService) {
        List<FileInfoDTO> imageUrls = pdf2jpg(tempFilePath, fileAttribute, fileService);
        if (imageUrls == null || imageUrls.size() < 1) {
            throw new ConvertException("PDF转图片异常，请联系管理员");
        }
        return imageUrls;
    }

    /**
     * PDF转图片列表
     *
     * @param tempFilePath 临时文件地址（PDF文件位置）
     * @param fileAttribute 文件属性
     * @param fileService 文件存储Service
     * @return java.util.List<top.krasus1966.common.file.entity.dto.FileInfoDTO>
     * @method pdf2jpg
     * @author krasus1966
     * @date 2023/5/7 02:38
     * @description PDF转图片列表
     */
    public static List<FileInfoDTO> pdf2jpg(String tempFilePath, FileAttribute fileAttribute, IFileService fileService) {
        List<FileChunkDTO> imageUrls = new ArrayList<>();
        File pdfFile = new File(tempFilePath);
        if (!pdfFile.exists()) {
            return null;
        }
        int index = tempFilePath.lastIndexOf(File.separator);
        String folder = tempFilePath.substring(0, index) + File.separator;
        File path = new File(folder);
        try (PDDocument doc = PDDocument.load(pdfFile)) {
            int pageCount = doc.getNumberOfPages();
            PDFRenderer pdfRenderer = new PDFRenderer(doc);
            if (!path.exists() && !path.mkdirs()) {
                log.error("PDF转图片失败：创建转换目录【{}】失败！", folder);
                throw new ConvertException("PDF转图片失败：创建转换目录失败！");
            }
            String imageFilePath;
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                imageFilePath = folder + fileAttribute.getFileName() + "_" + pageIndex + pdf2jpg_image_format;
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 105, ImageType.RGB);
                ImageIOUtil.writeImage(image, imageFilePath, 105);
                FileChunkDTO fileChunkDTO = new FileChunkDTO();
                File imageFile = new File(imageFilePath);
                fileChunkDTO.setFile(new CommonMultipartFile(imageFile, "image/jpeg"));
                fileChunkDTO.setMd5(MD5.create().digestHex16(imageFile));
                fileChunkDTO.setFileOriginalId(fileAttribute.getFileId());
                fileChunkDTO.setPreviewType(FilePreviewType.IMAGE);
                fileChunkDTO.setSort(pageIndex);
                imageUrls.add(fileChunkDTO);
            }
            // 单独存储转码的PDF
            FileChunkDTO fileChunkDTO = new FileChunkDTO();
            fileChunkDTO.setFile(new CommonMultipartFile(pdfFile, "application/pdf"));
            fileChunkDTO.setMd5(MD5.create().digestHex16(pdfFile));
            fileChunkDTO.setFileOriginalId(fileAttribute.getFileId());
            fileChunkDTO.setPreviewType(FilePreviewType.PDF);
            fileService.store(Collections.singletonList(fileChunkDTO));

            return fileService.store(imageUrls);
        } catch (IOException e) {
            log.error("PDF转图片失败，PDF文件地址：{}", tempFilePath, e);
            throw new ConvertException("PDF转图片失败！");
        } finally {
            if (path != null && path.exists()) {
                File[] files = path.listFiles();
                for (File file:files) {
                    file.delete();
                }
                path.delete();
            }
            if (null != pdfFile && pdfFile.exists()) {
                pdfFile.delete();
            }
        }
    }
}
