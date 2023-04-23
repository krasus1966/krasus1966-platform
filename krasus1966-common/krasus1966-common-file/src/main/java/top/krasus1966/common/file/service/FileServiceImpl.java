package top.krasus1966.common.file.service;


import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileChunkResultDTO;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.entity.dto.PageResultDTO;

import java.io.IOException;
import java.util.List;

/**
 * @author Krasus1966
 * {@code @date} 2023/4/5 00:24
 **/
public class FileServiceImpl implements IFileService {

    private final IFileStoreService fileStoreService;
    private final IFileQueryService fileQueryService;
    private final IFileChunkService fileChunkService;

    public FileServiceImpl(IFileStoreService fileStoreService, IFileQueryService fileQueryService
            , IFileChunkService fileChunkService) {
        this.fileStoreService = fileStoreService;
        this.fileQueryService = fileQueryService;
        this.fileChunkService = fileChunkService;
    }


    @Override
    public List<FileInfoDTO> store(List<FileChunkDTO> files) throws IOException {
        return fileStoreService.store(files);
    }

    @Override
    public FileInfoDTO checkFileExists(FileChunkDTO file) throws IOException {
        return fileStoreService.checkFileExists(file);
    }

    @Override
    public void delete(String ids) {
        fileStoreService.delete(ids);
    }

    @Override
    public List<FileInfoDTO> query(FileChunkDTO fileInfo) throws IOException {
        return fileQueryService.query(fileInfo);
    }

    @Override
    public PageResultDTO<FileInfoDTO> queryPage(FileChunkDTO fileInfo, long page, long pageSize) {
        return fileQueryService.queryPage(fileInfo, page, pageSize);
    }

    @Override
    public List<FileInfoDTO> findInputStream(FileChunkDTO fileInfo) throws IOException {
        return fileQueryService.findInputStream(fileInfo);
    }

    @Override
    public FileInfoDTO getOneById(String id) {
        return fileQueryService.getOneById(id);
    }


    @Override
    public FileChunkResultDTO chunkIsExists(FileChunkDTO fileChunkDTO) throws IOException {
        return fileChunkService.chunkIsExists(fileChunkDTO);
    }

    @Override
    public void uploadChunk(FileChunkDTO fileChunkDTO) {
        fileChunkService.uploadChunk(fileChunkDTO);
    }

    @Override
    public Boolean mergeChunk(FileChunkDTO fileChunkDTO) {
        return fileChunkService.mergeChunk(fileChunkDTO);
    }
}
