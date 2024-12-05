package top.krasus1966.common.file.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @author Krasus1966
 * {@code @date} 2023/4/4 00:19
 **/
@Data
@Accessors(chain = true)
public class FileInfoDTO {
    private String fileId;

    private String fileName;

    private Long fileLength;

    private String contentType;

    private LocalDateTime crtTime;

    private String md5;

    private Integer sort;

    private InputStream inputStream;

    private String previewType;
}
