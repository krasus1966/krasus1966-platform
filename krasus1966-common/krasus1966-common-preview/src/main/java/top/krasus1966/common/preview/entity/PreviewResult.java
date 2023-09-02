package top.krasus1966.common.preview.entity;

import lombok.Data;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/9/2 15:45
 **/
@Data
public class PreviewResult {

    private List<String> previewTypes;

    private List<FileInfoDTO> fileList;

    public PreviewResult() {
    }

    public PreviewResult(List<String> previewTypes, List<FileInfoDTO> fileList) {
        this.previewTypes = previewTypes;
        this.fileList = fileList;
    }
}
