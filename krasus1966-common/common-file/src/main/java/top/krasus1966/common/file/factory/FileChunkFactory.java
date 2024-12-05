package top.krasus1966.common.file.factory;


import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Krasus1966
 * {@code @date} 2022/11/16 20:26
 **/
public class FileChunkFactory implements IFileInfoFactory<FileChunkDTO> {

    private static volatile FileChunkFactory fileChunkFactory;

    public static IFileInfoFactory<FileChunkDTO> getInstance() {
        if (null == fileChunkFactory) {
            synchronized (FileChunkFactory.class) {
                if (null == fileChunkFactory) {
                    fileChunkFactory = new FileChunkFactory();
                }
            }
        }
        return fileChunkFactory;
    }

    @Override
    public FileInfoDTO toFileInfo(FileChunkDTO fileChunkDTO) {
        try {
            return new FileInfoDTO()
                    .setFileId(fileChunkDTO.getFileId())
                    .setFileName(fileChunkDTO.getFileName())
                    .setFileLength(fileChunkDTO.getTotalSize())
                    .setContentType(fileChunkDTO.getContentType())
                    .setCrtTime(fileChunkDTO.getCrtTime())
                    .setMd5(fileChunkDTO.getMd5())
                    .setInputStream(null != fileChunkDTO.getFile() ? fileChunkDTO.getFile().getInputStream() : null);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public List<FileInfoDTO> toFileInfoList(List<FileChunkDTO> datas) {
        return datas.stream().map(this::toFileInfo).collect(Collectors.toList());
    }

    @Override
    public FileChunkDTO fileInfoToData(FileInfoDTO fileInfo) {
        return new FileChunkDTO()
                .setFileId(fileInfo.getFileId())
                .setFileName(fileInfo.getFileName())
                .setTotalSize(fileInfo.getFileLength())
                .setContentType(fileInfo.getContentType())
                .setCrtTime(fileInfo.getCrtTime())
                .setMd5(fileInfo.getMd5());
    }

    @Override
    public List<FileChunkDTO> fileInfoToDatas(List<FileInfoDTO> fileInfos) {
        return fileInfos.stream().map(this::fileInfoToData).collect(Collectors.toList());
    }
}
