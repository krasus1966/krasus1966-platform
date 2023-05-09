package top.krasus1966.common.file.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * 分片文件信息
 *
 * @author Krasus1966
 * {@code @date} 2023/4/4 00:09
 **/
@Data
public class FileChunkDTO {

    /**
     * 分片文件
     */
    MultipartFile file;
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

    public String getFileId() {
        return fileId;
    }

    public FileChunkDTO setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public MultipartFile getFile() {
        return file;
    }

    public FileChunkDTO setFile(MultipartFile file) {
        this.file = file;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public FileChunkDTO setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public Integer getChunkNumber() {
        return chunkNumber;
    }

    public FileChunkDTO setChunkNumber(Integer chunkNumber) {
        this.chunkNumber = chunkNumber;
        return this;
    }

    public Long getChunkSize() {
        return chunkSize;
    }

    public FileChunkDTO setChunkSize(Long chunkSize) {
        this.chunkSize = chunkSize;
        return this;
    }

    public Long getCurrentChunkSize() {
        return currentChunkSize;
    }

    public FileChunkDTO setCurrentChunkSize(Long currentChunkSize) {
        this.currentChunkSize = currentChunkSize;
        return this;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public FileChunkDTO setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    public Integer getTotalChunks() {
        return totalChunks;
    }

    public FileChunkDTO setTotalChunks(Integer totalChunks) {
        this.totalChunks = totalChunks;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileChunkDTO setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public FileChunkDTO setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public LocalDateTime getCrtTime() {
        return crtTime;
    }

    public FileChunkDTO setCrtTime(LocalDateTime crtTime) {
        this.crtTime = crtTime;
        return this;
    }
}
