package top.krasus1966.common.file.service;


import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileChunkResultDTO;

import java.io.IOException;

/**
 * 文件分片相关接口
 *
 * @author Krasus1966
 * {@code @date} 2023/4/4 22:55
 **/
public interface IFileChunkService {

    /**
     * 分片上传：检查分片是否已经存在
     *
     * @param fileChunkDTO 分片文件信息
     * @return top.krasus1966.file_server.entity.dto.FileChunkResultDTO
     * @throws
     * @method chunkIsExists
     * @author krasus1966
     * {@code @date} 2023/4/4 00:25
     * @description 分片上传：检查分片是否已经存在
     */
    FileChunkResultDTO chunkIsExists(FileChunkDTO fileChunkDTO) throws IOException;

    /**
     * 分片上传：上传分片文件
     *
     * @param fileChunkDTO 分片文件信息
     * @return top.krasus1966.file_server.entity.dto.FileChunkResultDTO
     * @throws
     * @method uploadChunk
     * @author krasus1966
     * {@code @date} 2023/4/4 00:26
     * @description 分片上传：上传分片文件
     */
    void uploadChunk(FileChunkDTO fileChunkDTO);

    /**
     * 分片上传：合并分片文件
     *
     * @param fileChunkDTO 分片文件信息
     * @return java.lang.Boolean
     * @throws
     * @method mergeChunk
     * @author krasus1966
     * {@code @date} 2023/4/4 00:30
     * @description 分片上传：合并分片文件
     */
    Boolean mergeChunk(FileChunkDTO fileChunkDTO);
}
