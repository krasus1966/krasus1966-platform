package top.krasus1966.common.preview.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.service.IFileService;
import top.krasus1966.common.preview.entity.FileAttribute;
import top.krasus1966.common.preview.entity.FilePreviewType;
import top.krasus1966.common.preview.entity.PreviewResult;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Krasus1966
 * @date 2023/9/2 18:08
 **/
@Service
@Slf4j
public class PictureFilePreviewService extends BaseFilePreviewService implements FilePreviewService{
    public PictureFilePreviewService(IFileService fileService) {
        super(fileService);
    }

    @Override
    public void allowPreviewTypes() {
        addAllowPreviewTypes(FilePreviewType.IMAGE);
    }

    @Override
    public PreviewResult filePreviewHandle(FileAttribute fileAttribute, FileInfoDTO fileInfoDTO) throws IOException {
        return new PreviewResult(getAllowPreviewType(), Collections.singletonList(fileInfoDTO));
    }
}
