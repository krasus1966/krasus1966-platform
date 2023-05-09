package top.krasus1966.common.file.entity.dto;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @author Krasus1966
 * {@code @date} 2023/4/4 00:19
 **/
public class FileInfoDTO {
    private String fileId;

    private String fileName;

    private Long fileLength;

    private String contentType;

    private LocalDateTime crtTime;

    private String md5;

    private Integer sort;

    private InputStream inputStream;

    public String getFileId() {
        return fileId;
    }

    public FileInfoDTO setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileInfoDTO setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public FileInfoDTO setFileLength(Long fileLength) {
        this.fileLength = fileLength;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public FileInfoDTO setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public LocalDateTime getCrtTime() {
        return crtTime;
    }

    public FileInfoDTO setCrtTime(LocalDateTime crtTime) {
        this.crtTime = crtTime;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public FileInfoDTO setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public Integer getSort() {
        return sort;
    }

    public FileInfoDTO setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public FileInfoDTO setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }
}
