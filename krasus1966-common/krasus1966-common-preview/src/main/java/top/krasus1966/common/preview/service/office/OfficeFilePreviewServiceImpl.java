package top.krasus1966.common.preview.service.office;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.office.OfficeException;
import org.springframework.stereotype.Service;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.service.IFileService;
import top.krasus1966.common.preview.entity.FileAttribute;
import top.krasus1966.common.preview.entity.FilePreviewType;
import top.krasus1966.common.preview.entity.PreviewResult;
import top.krasus1966.common.preview.entity.ResultMsg;
import top.krasus1966.common.preview.exception.ConvertException;
import top.krasus1966.common.preview.service.BaseFilePreviewService;
import top.krasus1966.common.preview.service.FilePreviewService;
import top.krasus1966.common.preview.service.office.util.OfficeConvertUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by kl on 2018/1/17.
 * Content :处理office文件
 */
@Service
@Slf4j
public class OfficeFilePreviewServiceImpl extends BaseFilePreviewService implements FilePreviewService {

    public OfficeFilePreviewServiceImpl(IFileService fileService) {
        super(fileService);
    }

    @Override
    public void allowPreviewTypes() {
        addAllowPreviewTypes(FilePreviewType.PDF);
        addAllowPreviewTypes(FilePreviewType.IMAGE);
    }

    @Override
    public PreviewResult filePreviewHandle(FileAttribute fileAttribute, FileInfoDTO fileInfoDTO) throws IOException {
        String previewType = StrUtil.isBlank(fileAttribute.getPreviewType()) ? getAllowPreviewType().get(0) : fileAttribute.getPreviewType();
        // 通过fileId查询是否已经有对应类型的预览文件
        List<FileInfoDTO> fileInfoDTOS = queryExistFile(fileAttribute, previewType);
        if (null != fileInfoDTOS && !fileInfoDTOS.isEmpty()) {
            return new PreviewResult(getAllowPreviewType(),fileInfoDTOS);
        }
        boolean needCreatePDF = true;
        String url = fileAttribute.getFileUrl();
        // 通过fileId查询是否有对应的PDF文件
        fileInfoDTOS = queryExistFile(fileAttribute, FilePreviewType.PDF);
        if (null != fileInfoDTOS && !fileInfoDTOS.isEmpty()) {
            String fileId = fileInfoDTOS.get(0).getFileId();
            url = fileAttribute.getDownloadUrl() + fileId;
            needCreatePDF = false;
        }
        File tempOutputFolder = new File(getTempPathName(fileAttribute, "download"));
        File tempFile = FileUtil.createTempFile(fileAttribute.getFileName(), "." + fileAttribute.getSuffix(), tempOutputFolder, true);
        try {
            HttpUtil.downloadFile(url, tempFile);
            if (!needCreatePDF) {
                return new PreviewResult(getAllowPreviewType(),OfficeConvertUtil.getPreviewType(fileAttribute, tempFile.getPath(), fileService));
            }
            String tempPDFPath = getTempPathName(fileAttribute, "pdf") + fileAttribute.getFileName() + ".pdf";
            ResultMsg resultMsg = OfficeConvertUtil.officeConvert2PDF(tempFile, tempPDFPath);
            if (ResultMsg.SUCCESS.equals(resultMsg.getCode())) {
                return new PreviewResult(getAllowPreviewType(),OfficeConvertUtil.getPreviewType(fileAttribute, tempPDFPath, fileService));
            }
            throw new ConvertException(resultMsg.getMsg());
        } catch (OfficeException e) {
            log.error("文件预览失败", e);
            throw new ConvertException("文件预览失败");
        } catch (HttpException e) {
            throw new ConvertException("获取源文件失败");
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            if (tempOutputFolder.exists()) {
                File[] files = tempOutputFolder.listFiles();
                for (File file : files) {
                    file.delete();
                }
                tempOutputFolder.delete();
            }
        }
    }
}
