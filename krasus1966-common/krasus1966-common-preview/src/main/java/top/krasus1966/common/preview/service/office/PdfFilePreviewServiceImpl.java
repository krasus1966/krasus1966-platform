package top.krasus1966.common.preview.service.office;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.service.IFileService;
import top.krasus1966.common.preview.entity.FileAttribute;
import top.krasus1966.common.preview.entity.FilePreviewType;
import top.krasus1966.common.preview.exception.ConvertException;
import top.krasus1966.common.preview.service.BaseFilePreviewService;
import top.krasus1966.common.preview.service.FilePreviewService;
import top.krasus1966.common.preview.service.office.util.OfficeConvertUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


/**
 * @author Krasus1966
 * @date 2023/5/7 16:27
 **/
@Service
@Slf4j
public class PdfFilePreviewServiceImpl extends BaseFilePreviewService implements FilePreviewService {
    public PdfFilePreviewServiceImpl(IFileService fileService) {
        super(fileService);
    }

    @Override
    public Object filePreviewHandle(FileAttribute fileAttribute,FileInfoDTO fileInfoDTO) throws IOException {
        String previewType = StrUtil.isBlank(fileAttribute.getPreviewType()) ? FilePreviewType.IMAGE : fileAttribute.getPreviewType();
        if (FilePreviewType.PDF.equals(previewType)) {
            // 直接返回文件
            return Collections.singletonList(fileInfoDTO);
        }
        List<FileInfoDTO> fileInfoDTOS = queryExistFile(fileAttribute, previewType);
        if (null != fileInfoDTOS && !fileInfoDTOS.isEmpty()) {
            return fileInfoDTOS;
        }
        String url = fileAttribute.getFileUrl();
        File tempOutputFolder = new File(getTempPathName(fileAttribute,"download"));
        File tempFile = FileUtil.createTempFile(fileAttribute.getFileName(), "." + fileAttribute.getSuffix(), tempOutputFolder, true);
        try {
            HttpUtil.downloadFile(url, tempFile);
            return OfficeConvertUtil.getPreviewType(fileAttribute, tempFile.getPath(), fileService);
        } catch (HttpException e) {
            throw new ConvertException("获取源文件失败");
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            if (tempOutputFolder.exists()) {
                File[] files = tempOutputFolder.listFiles();
                for (File file:files) {
                    file.delete();
                }
                tempOutputFolder.delete();
            }
        }
    }
}
