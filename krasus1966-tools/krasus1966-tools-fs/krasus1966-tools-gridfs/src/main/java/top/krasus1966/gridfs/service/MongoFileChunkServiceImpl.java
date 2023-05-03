package top.krasus1966.gridfs.service;


import cn.hutool.core.io.IoUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileChunkResultDTO;
import top.krasus1966.common.file.service.IFileChunkService;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.spring.i18n.util.I18NUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Krasus1966
 * {@code @date} 2022/11/2 16:10
 **/
public class MongoFileChunkServiceImpl extends AbstractMongoFileServiceImpl implements IFileChunkService {

    @Value("${temp.chunk_temp_file_path}")
    private String chunkTempFilePath;

    public MongoFileChunkServiceImpl(GridFsTemplate gridFsTemplate) {
        super(gridFsTemplate);
    }

    @Override
    public FileChunkResultDTO chunkIsExists(FileChunkDTO fileChunkDTO) throws IOException {
        if (null == fileChunkDTO) {
            throw new BizException(I18NUtils.getMessage("file.upload_file_not_exists","上传文件为空"));
        }
        if (null == fileChunkDTO.getMd5() || "".equals(fileChunkDTO.getMd5())) {
            throw new BizException(I18NUtils.getMessage("file.upload_file_not_exists","上传文件为空"));
        }
        // 检查合并文件是否存在
        if (null != checkFileExists(fileChunkDTO)) {
            return new FileChunkResultDTO(true);
        }
        String tempFileFolderPath =
                chunkTempFilePath + File.separator + fileChunkDTO.getMd5() + File.separator;

        File tempFolder = new File(tempFileFolderPath);
        // 分片目录不存在，代表没有上传，创建目录并返回
        if (tempFolder.mkdirs()) {
            return new FileChunkResultDTO(false);
        }
        // 检查已经存在的分片文件
        File[] chunks = tempFolder.listFiles();
        Set<Integer> chunkNumber = new HashSet<>();
        if (null != chunks) {
            for (File chunk : chunks) {
                String chunkName = chunk.getName().substring(0, chunk.getName().indexOf("."));
                chunkNumber.add(Integer.valueOf(chunkName));
            }
        }
        return new FileChunkResultDTO(false, chunkNumber);
    }

    @Override
    public void uploadChunk(FileChunkDTO fileChunkDTO) {
        if (null == fileChunkDTO) {
            throw new BizException(I18NUtils.getMessage("file.upload_file_not_exists","上传文件为空"));
        }
        if (null == fileChunkDTO.getMd5() || "".equals(fileChunkDTO.getMd5())) {
            throw new BizException(I18NUtils.getMessage("file.upload_file_not_exists","上传文件为空"));
        }
        String tempFileFolderPath =
                chunkTempFilePath + File.separator + fileChunkDTO.getMd5() + File.separator;
        File tempFolder = new File(tempFileFolderPath);
        // 分片目录不存在，代表没有上传，创建目录并返回
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
        try (InputStream inputStream = fileChunkDTO.getFile().getInputStream();
             FileOutputStream outputStream =
                     new FileOutputStream(tempFileFolderPath + fileChunkDTO.getChunkNumber() +
                             ".temp")) {
            IoUtil.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean mergeChunk(FileChunkDTO fileChunkDTO) {
        if (null == fileChunkDTO) {
            throw new BizException(I18NUtils.getMessage("file.upload_file_not_exists","上传文件为空"));
        }
        if (null == fileChunkDTO.getMd5() || "".equals(fileChunkDTO.getMd5())) {
            throw new BizException(I18NUtils.getMessage("file.upload_file_not_exists","上传文件为空"));
        }
        String tempFileFolderPath =
                chunkTempFilePath + File.separator + fileChunkDTO.getMd5() + File.separator;
        // 检查分片是否都存在
        if (checkChunksExists(tempFileFolderPath, fileChunkDTO.getTotalChunks())) {
            File chunkFileFolder = new File(tempFileFolderPath);
            File mergeFile = new File(tempFileFolderPath + fileChunkDTO.getFileName());
            if (mergeFile.exists()) {
                mergeFile.delete();
            }
            File[] chunks = chunkFileFolder.listFiles();
            //排序
            List<File> fileList = Arrays.asList(chunks);
            fileList =
                    fileList.stream().filter(file -> file.getName().contains(".temp")).collect(Collectors.toList());
            fileList.sort(Comparator.comparingInt((File chunk) -> Integer.parseInt(chunk.getName().substring(0, chunk.getName().indexOf(".")))));
            try {
                RandomAccessFile randomAccessFileWriter = new RandomAccessFile(mergeFile, "rw");
                byte[] bytes = new byte[1024];
                for (File chunk : fileList) {
                    RandomAccessFile randomAccessFileReader = new RandomAccessFile(chunk, "r");
                    int len;
                    while ((len = randomAccessFileReader.read(bytes)) != -1) {
                        randomAccessFileWriter.write(bytes, 0, len);
                    }
                    randomAccessFileReader.close();
                }
                randomAccessFileWriter.close();

                // 存储到MongoDB中
                // 存储到GridFS
                ObjectId objectId = gridFsTemplate.store(Files.newInputStream(mergeFile.toPath())
                        , fileChunkDTO.getFileName(), fileChunkDTO);
                fileChunkDTO.setFileId(objectId.toHexString());
            } catch (Exception e) {
                return false;
            } finally {
                if (chunkFileFolder.exists()) {
                    File[] files = chunkFileFolder.listFiles();
                    for (File file : files) {
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    chunkFileFolder.delete();
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkChunksExists(String filePath, Integer totalChunks) {
        for (int i = 1; i <= totalChunks; i++) {
            File file = new File(filePath + File.separator + i + ".temp");
            if (!file.exists()) {
                return false;
            }
        }
        return true;
    }
}
