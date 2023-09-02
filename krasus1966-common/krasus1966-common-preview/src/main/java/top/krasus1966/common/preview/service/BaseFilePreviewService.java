package top.krasus1966.common.preview.service;

import cn.hutool.core.util.IdUtil;
import jakarta.validation.constraints.NotNull;
import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.service.IFileService;
import top.krasus1966.common.preview.entity.FileAttribute;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Krasus1966
 * @date 2023/5/7 16:51
 **/
public abstract class BaseFilePreviewService {
    protected final IFileService fileService;

    private final List<String> allowPreviewTypes = new ArrayList<>();

    public BaseFilePreviewService(IFileService fileService) {
        this.fileService = fileService;
        allowPreviewTypes();
    }

    public abstract void allowPreviewTypes();

    public void addAllowPreviewTypes(String type) {
        allowPreviewTypes.add(type);
    }

    public List<String> getAllowPreviewType() {
        return allowPreviewTypes;
    }

    protected List<FileInfoDTO> queryExistFile(FileAttribute fileAttribute,String previewType) throws IOException {
        // 通过fileId查询是否已经有对应类型的预览文件
        FileChunkDTO fileChunkDTO = new FileChunkDTO();
        fileChunkDTO.setFileOriginalId(fileAttribute.getFileId());
        fileChunkDTO.setPreviewType(previewType);
        List<FileInfoDTO> query = fileService.query(fileChunkDTO);
        if (null != query && !query.isEmpty()) {
            return query.stream().sorted(Comparator.comparing(FileInfoDTO::getSort)).peek(item -> item.setPreviewType(previewType)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @NotNull
    protected static String getTempPathName(FileAttribute fileAttribute,String tempPathName) {
        return fileAttribute.getOutputPath() + tempPathName + File.separator + fileAttribute.getFileName() + IdUtil.simpleUUID() + File.separator;
    }

    protected String textData(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line).append("\r\n");
        }
        br.close();
        return result.toString();
    }
}
