package top.krasus1966.gridfs.service;


import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.core.exception.BizException;
import top.krasus1966.core.util.i18n.I18NUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * {@code @date} 2022/11/2 16:10
 **/
public abstract class AbstractMongoFileServiceImpl {

    protected final GridFsTemplate gridFsTemplate;

    protected AbstractMongoFileServiceImpl(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public FileInfoDTO checkFileExists(FileChunkDTO file) throws IOException {
        // 文件不存在跳过
//        if (null == file.getFile()) {
//            throw new BizException(I18NUtils.getMessage("file.upload_file_not_exists"));
//        }
        // 通过摘要信息查询文件是否存在，存在则不再继续存储
//        String md5 =
//                Arrays.toString(new Binary(BsonBinarySubType.MD5, file.getFile().getBytes())
//                .getData());
        Query query = Query.query(Criteria.where("md5").is(file.getMd5()));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        if (null != gridFSFile) {
            return gridFs2FileInfoDTO(false, gridFSFile);
        }
        return null;
    }

    protected FileInfoDTO getOneById(String id, boolean getInputStream) {
        if (null == id || "".equals(id)) {
            throw new BizException(I18NUtils.getMessage("param.id_not_exist"));
        }
        Query gridQuery = new Query().addCriteria(Criteria.where("_id").is(id));
        GridFSFile gridFsFile = gridFsTemplate.findOne(gridQuery);
        if (null == gridFsFile) {
            throw new BizException(I18NUtils.getMessage("file.file_not_exists"));
        }
        try {
            return gridFs2FileInfoDTO(getInputStream, gridFsFile);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 查询文件列表
     *
     * @param fileInfo       前端传入信息
     * @param getInputStream 是否获取输入流
     * @return java.util.List<top.krasus1966.file_server.entity.dto.FileInfoDTO>
     * @throws IOException IO异常
     * @method queryFileInfo
     * @author krasus1966
     * {@code @date} 2023/4/4 23:29
     * @description 查询文件列表
     */
    protected List<FileInfoDTO> queryFileInfo(FileChunkDTO fileInfo, boolean getInputStream) throws IOException {
        Query query = new Query();
        if (null != fileInfo.getFileId() && !"".equals(fileInfo.getFileId().trim())) {
            String[] ids = fileInfo.getFileId().split(",");
            query.addCriteria(Criteria.where("id").in(ids));
        }
        GridFSFindIterable gridFSFiles = gridFsTemplate.find(query);
        List<FileInfoDTO> fileList = new ArrayList<FileInfoDTO>();
        for (GridFSFile gridFSFile : gridFSFiles) {
            FileInfoDTO fileInfoDTO = gridFs2FileInfoDTO(getInputStream, gridFSFile);
            fileList.add(fileInfoDTO);
        }
        return fileList;
    }

    /**
     * GridFs对象转FileInfoDTO
     *
     * @param getInputStream
     * @param gridFSFile
     * @return top.krasus1966.file_server.entity.dto.FileInfoDTO
     * @throws IOException
     * @method getFileInfoDTO
     * @author krasus1966
     * {@code @date} 2023/4/4 23:45
     * @description GridFs对象转FileInfoDTO
     */
    protected FileInfoDTO gridFs2FileInfoDTO(boolean getInputStream, GridFSFile gridFSFile) throws IOException {
        Instant instant = gridFSFile.getUploadDate().toInstant();
        FileInfoDTO fileInfoDTO = new FileInfoDTO()
                .setFileId(gridFSFile.getObjectId().toHexString())
                .setFileName(gridFSFile.getFilename())
                .setFileLength(gridFSFile.getLength())
                .setContentType(null != gridFSFile.getMetadata() ?
                        gridFSFile.getMetadata().getString("_contentType") : "")
                .setCrtTime(instant.atZone(ZoneId.systemDefault()).toLocalDateTime());
        //获取流对象
        if (getInputStream) {
            GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
            if (resource.exists()) {
                fileInfoDTO.setInputStream(resource.getInputStream());
            }
        }
        return fileInfoDTO;
    }
}
