package top.krasus1966.common.preview.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.service.IFileService;
import top.krasus1966.common.preview.entity.FileAttribute;
import top.krasus1966.common.preview.entity.FilePreviewType;
import top.krasus1966.common.preview.entity.PreviewResult;

import java.util.Arrays;

/**
 * @author Krasus1966
 * @date 2023/5/7 18:02
 **/
@Service
@Slf4j
public class TextFilePreviewService extends BaseFilePreviewService implements FilePreviewService{
    public TextFilePreviewService(IFileService fileService) {
        super(fileService);
    }

    @Override
    public void allowPreviewTypes() {
        addAllowPreviewTypes(FilePreviewType.TXT);
        addAllowPreviewTypes(FilePreviewType.HTML);
    }

    @Override
    public PreviewResult filePreviewHandle(FileAttribute fileAttribute, FileInfoDTO fileInfoDTO) {
        String previewType = StrUtil.isBlank(fileAttribute.getPreviewType()) ? getAllowPreviewType().get(0) : fileAttribute.getPreviewType();
        return new PreviewResult(getAllowPreviewType(),Arrays.asList(fileInfoDTO));
        /*
        // 通过fileId查询是否已经有对应类型的预览文件
        FileChunkDTO fileChunkDTO = new FileChunkDTO();
        fileChunkDTO.setFileOriginalId(fileAttribute.getFileId());
        fileChunkDTO.setPreviewType(previewType);
        List<FileInfoDTO> query = fileService.query(fileChunkDTO);
        if (null != query && !query.isEmpty()) {
            return query.stream().sorted(Comparator.comparing(FileInfoDTO::getSort)).collect(Collectors.toList());
        }
        File tempOutputFolder = new File(getTempPathName(fileAttribute, "html"));
        File tempFile = FileUtil.createTempFile(fileAttribute.getFileName(), "." + fileAttribute.getSuffix(), tempOutputFolder, true);
        try {
            String data = HtmlUtils.htmlEscape(textData(fileInfoDTO.getInputStream()));
            FileUtil.appendString(data,tempFile,StandardCharsets.UTF_8);
            // 单独存储转码的文件
            fileChunkDTO = new FileChunkDTO();
            fileChunkDTO.setFile(new CommonMultipartFile(tempFile, "text/plain"));
            fileChunkDTO.setMd5(MD5.create().digestHex16(tempFile));
            fileChunkDTO.setFileOriginalId(fileAttribute.getFileId());
            fileChunkDTO.setPreviewType(previewType);
            return fileService.store(Collections.singletonList(fileChunkDTO));
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }

         */
    }
}
