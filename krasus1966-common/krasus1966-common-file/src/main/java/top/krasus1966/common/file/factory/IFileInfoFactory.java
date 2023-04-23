package top.krasus1966.common.file.factory;


import top.krasus1966.common.file.entity.dto.FileInfoDTO;

import java.io.IOException;
import java.util.List;

/**
 * @author Krasus1966
 * {@code @date} 2022/11/16 20:24
 **/
public interface IFileInfoFactory<T> {

    FileInfoDTO toFileInfo(T t) throws IOException;

    List<FileInfoDTO> toFileInfoList(List<T> datas);

    T fileInfoToData(FileInfoDTO fileInfo);

    List<T> fileInfoToDatas(List<FileInfoDTO> fileInfos);
}
