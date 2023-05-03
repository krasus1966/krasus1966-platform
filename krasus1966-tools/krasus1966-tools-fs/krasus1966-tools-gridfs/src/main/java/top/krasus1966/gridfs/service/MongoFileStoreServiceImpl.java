package top.krasus1966.gridfs.service;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.factory.FileChunkFactory;
import top.krasus1966.common.file.service.IFileStoreService;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.spring.i18n.util.I18NUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Krasus1966
 * {@code @date} 2022/11/2 16:10
 **/
public class MongoFileStoreServiceImpl extends AbstractMongoFileServiceImpl implements IFileStoreService {


    public MongoFileStoreServiceImpl(GridFsTemplate gridFsTemplate) {
        super(gridFsTemplate);
    }

    @Override
    public List<FileInfoDTO> store(List<FileChunkDTO> files) throws IOException {
        List<FileInfoDTO> idList = new ArrayList<>();
        for (FileChunkDTO file : files) {
            // 文件不存在跳过
            if (null == file.getFile()) {
                continue;
            }
            // 文件存在跳过上传
            FileInfoDTO fileInfoDTO = checkFileExists(file);
            if (null != fileInfoDTO) {
                idList.add(fileInfoDTO);
                continue;
            }
            // 存储到GridFS
            ObjectId objectId =
                    gridFsTemplate.store(new ByteArrayInputStream(file.getFile().getBytes()),
                            file.getFileName(),
                            new FileChunkDTO());
            file.setFileId(objectId.toHexString());
            file.setFile(null);
            idList.add(FileChunkFactory.getInstance().toFileInfo(file));
        }
        return idList;
    }

    @Override
    public FileInfoDTO checkFileExists(FileChunkDTO file) throws IOException {
        return super.checkFileExists(file);
    }

    @Override
    public void delete(String ids) {
        Query query = new Query();
        if (null == ids || "".equals(ids.trim())) {
            throw new BizException(I18NUtils.getMessage("param.ids_not_exist","ids不能为空"));
        }
        String[] fileIds = ids.split(",");
        query.addCriteria(Criteria.where("id").in(fileIds));
        gridFsTemplate.delete(query);
    }
}
