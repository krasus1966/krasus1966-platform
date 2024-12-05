package top.krasus1966.common.file.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * 分片文件信息
 *
 * @author Krasus1966
 * {@code @date} 2023/4/4 00:09
 **/
@Data
@Accessors(chain = true)
public class FileChunkDTO {

    /**
     * 分片文件
     */
    private MultipartFile file;
    private String fileId;
    /**
     * 分片MD5值
     */
    private String md5;
    /**
     * 分片号
     */
    private Integer chunkNumber;
    /**
     * 分片大小
     */
    private Long chunkSize;
    /**
     * 当前分片大小
     */
    private Long currentChunkSize;
    /**
     * 文件总大小
     */
    private Long totalSize;
    /**
     * 分片总数
     */
    private Integer totalChunks;
    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String contentType;

    private LocalDateTime crtTime;

    /**
     * 预览相关
     */
    /**
     * 源文件id
     */
    private String fileOriginalId;
    /**
     * 预览类型
     */
    private String previewType;
    /**
     * 排序
     */
    private Integer sort;

    public FileChunkDTO() {
    }

    public FileChunkDTO(MultipartFile file) {
        this.file = file;
    }
}
