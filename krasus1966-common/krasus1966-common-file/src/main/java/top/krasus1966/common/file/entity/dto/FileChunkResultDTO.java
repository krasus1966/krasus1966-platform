package top.krasus1966.common.file.entity.dto;

import java.util.Set;

/**
 * @author Krasus1966
 * {@code @date} 2023/4/4 00:12
 **/
public class FileChunkResultDTO {

    private boolean skip = false;

    private Set<Integer> uploaded;

    public FileChunkResultDTO() {
    }

    public FileChunkResultDTO(boolean skip) {
        this.skip = skip;
    }

    public FileChunkResultDTO(boolean skip, Set<Integer> uploaded) {
        this.skip = skip;
        this.uploaded = uploaded;
    }

    public boolean isSkip() {
        return skip;
    }

    public FileChunkResultDTO setSkip(boolean skip) {
        this.skip = skip;
        return this;
    }

    public Set<Integer> getUploaded() {
        return uploaded;
    }

    public FileChunkResultDTO setUploaded(Set<Integer> uploaded) {
        this.uploaded = uploaded;
        return this;
    }
}
