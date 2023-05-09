package top.krasus1966.common.preview.entity;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author Krasus1966
 * @date 2023/5/7 01:18
 **/
public class CommonMultipartFile implements MultipartFile {

    private final File file;
    private final String contentType;

    public CommonMultipartFile(File file,String contentType) {
        this.file = file;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getOriginalFilename() {
        return file.getName();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        if (null == file || !file.exists()) {
            return true;
        }
        return false;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return FileCopyUtils.copyToByteArray(this.getInputStream());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(file.toPath());
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.getInputStream(), Files.newOutputStream(dest.toPath()));
    }
}
