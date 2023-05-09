package top.krasus1966.common.preview.entity;

import lombok.Data;

/**
 * @author Krasus1966
 * @date 2023/5/7 00:09
 **/
@Data
public class FileAttribute {

    private String fileId;

    private String downloadUrl;
    private String fileUrl;

    private FileType fileType;

    private String fileName;

    private String suffix;

    private String md5;

    private String outputPath;

    private String previewType;
}
