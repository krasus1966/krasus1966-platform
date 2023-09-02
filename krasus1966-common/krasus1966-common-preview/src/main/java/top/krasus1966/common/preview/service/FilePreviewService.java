package top.krasus1966.common.preview.service;

import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.preview.entity.FileAttribute;
import top.krasus1966.common.preview.entity.PreviewResult;

import java.io.IOException;

/**
 * @author Krasus1966
 * @date 2023/5/6 23:41
 **/
public interface FilePreviewService {

    PreviewResult filePreviewHandle(FileAttribute fileAttribute, FileInfoDTO fileInfoDTO) throws IOException;
}
