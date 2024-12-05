package top.krasus1966.common.file.service;


import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.entity.dto.PageResultDTO;

import java.io.IOException;
import java.util.List;

/**
 * 文件查询相关接口
 *
 * @author Krasus1966
 * {@code @date} 2023/4/4 22:55
 **/
public interface IFileQueryService {
    /**
     * 列表查询文件
     *
     * @param fileInfo 文件信息
     * @return java.util.List<top.krasus1966.base_project.mongo.entity.file.FileDocument>
     * @method query
     * @author krasus1966
     * {@code @date} 2022/11/2 16:12
     * @description 列表查询文件
     */
    List<FileInfoDTO> query(FileChunkDTO fileInfo) throws IOException;

    /**
     * 分页查询文件
     *
     * @param fileInfo 文件信息
     * @return List<FileDocument>
     * @throws
     * @method queryPage
     * @author krasus1966
     * {@code @date} 2022/11/2 20:22
     * @description 分页查询文件
     */
    PageResultDTO<FileInfoDTO> queryPage(FileChunkDTO fileInfo, long page, long pageSize);

    /**
     * 通过文件列表获取流
     *
     * @param fileInfos 文件列表
     * @return java.util.List<top.krasus1966.base_project.mongo.entity.file.FileDocument>
     * @method findInputStream
     * @author krasus1966
     * {@code @date} 2022/11/2 21:21
     * @description 通过文件列表获取流
     */
    List<FileInfoDTO> findInputStream(FileChunkDTO fileInfo) throws IOException;

    /**
     * 通过id查询文件
     *
     * @param id 文件id
     * @return top.krasus1966.base_project.mongo.entity.file.FileDocument
     * @method getOneById
     * @author krasus1966
     * {@code @date} 2022/11/2 16:12
     * @description 通过id查询文件
     */
    FileInfoDTO getOneById(String id);
}
