package top.krasus1966.common.file.service;


import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;

import java.io.IOException;
import java.util.List;

/**
 * 文件存储相关接口
 *
 * @author Krasus1966
 * {@code @date} 2023/4/4 22:55
 **/
public interface IFileStoreService {
    /**
     * 直接存储文件，支持批量
     *
     * @return java.util.List<top.krasus1966.base_project.mongo.entity.file.FileDocument>
     * @method store
     * @author krasus1966
     * {@code @date} 2022/11/2 16:11
     * @description 存储文件，支持批量
     */
    List<FileInfoDTO> store(List<FileChunkDTO> files) throws IOException;

    /**
     * 检查文件是否存在
     *
     * @param file 上传文件信息
     * @return boolean
     * @throws
     * @method checkFileExists
     * @author krasus1966
     * {@code @date} 2023/4/4 22:58
     * @description 检查文件是否存在
     */
    FileInfoDTO checkFileExists(FileChunkDTO file) throws IOException;

    /**
     * 删除文件
     *
     * @param id 文件id
     * @return void
     * @method delete
     * @author krasus1966
     * {@code @date} 2022/11/2 20:46
     * @description 删除文件
     */
    void delete(String ids);
}
