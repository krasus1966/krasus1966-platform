package top.krasus1966.common.file.facade;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import top.krasus1966.common.core.entity.R;
import top.krasus1966.common.core.exception.BizException;
import top.krasus1966.common.core.func.BaseFunction;
import top.krasus1966.common.file.entity.dto.FileChunkDTO;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.entity.dto.PageResultDTO;
import top.krasus1966.common.file.service.IFileService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author Krasus1966
 * {@code @date} 2022/11/2 20:18
 **/
@RestController
@Slf4j
@RequestMapping("/file/download")
@CrossOrigin("*")
@ConditionalOnProperty(prefix = "file.facade", name = "enabled", havingValue = "true")
public class DownloadController extends BaseFunction {


    private final IFileService fileService;

    public DownloadController(IFileService fileService) {
        this.fileService = fileService;
    }


    @RequestMapping("/queryPage")
    public R<PageResultDTO<FileInfoDTO>> queryPage(FileChunkDTO fileChunkDTO, Integer page, Integer pageSize) throws IOException {
        return R.success(fileService.queryPage(fileChunkDTO, page, pageSize));
    }

    /**
     * 通用文件下载
     *
     * @param fileId 文件id
     * @return void
     * @method download
     * @author krasus1966
     * {@code @date} 2022/1/19 09:53
     * @description 通用文件下载
     */
    @RequestMapping("/{fileId}")
    public void download(@PathVariable("fileId") String fileId) {
        if (CharSequenceUtil.isBlank(fileId)) {
            throw new BizException("文件id不能为空");
        }
        FileInfoDTO fileInfo = fileService.getOneById(fileId);
        if (null == fileInfo) {
            return;
        }
        super.downloadFile(fileInfo.getInputStream(), fileInfo.getFileName(), fileInfo.getFileLength());
    }

    /**
     * 下载压缩包
     *
     * @param fileIds  文件id
     * @param fileName 文件名
     * @return void
     * @method downloadZip
     * @author krasus1966
     * {@code @date} 2022/1/19 09:54
     * @description 下载压缩包
     */
    @GetMapping("/downloadZip")
    public void downloadZip(String fileIds, String fileName) throws IOException {
        if (CharSequenceUtil.isBlank(fileIds)) {
            throw new BizException("文件id不能为空");
        }
        FileChunkDTO fileChunkDTO = new FileChunkDTO();
        fileChunkDTO.setFileId(fileIds);
        List<FileInfoDTO> fileDocuments = fileService.findInputStream(fileChunkDTO);
        StringJoiner paths = new StringJoiner(",");
        List<InputStream> list = new ArrayList<>();
        for (FileInfoDTO file : fileDocuments) {
            if (file.getInputStream() != null) {
                paths.add(file.getFileName());
                list.add(file.getInputStream());
            }
        }
        if (list.isEmpty()) {
            throw new BizException("文件不存在");
        }
        InputStream[] inputStreams = new InputStream[list.size()];
        File zipFile = ZipUtil.zip(File.createTempFile(UUID.randomUUID().toString(), ".tmp"), paths.toString().split(
                ","), list.toArray(inputStreams));


        response.setHeader("Content-disposition",
                "attachment;filename=" + URLEncoder.encode(StrUtil.isBlankIfStr(fileName) ?
                        System.currentTimeMillis() + ".zip" : fileName + ".zip", StandardCharsets.UTF_8));
        response.setContentType("application/x-zip-compressed");
        try (InputStream is = Files.newInputStream(zipFile.toPath())) {
            super.downloadFile(is, zipFile.getName(), zipFile.length());
        } catch (Exception e) {
            if (!(e instanceof IOException)) {
                log.error("/file/download/downloadZip ERROR", e);
            }
        } finally {
            // 判断文件是否存在并执行删除，如果删除失败，加入到虚拟机结束时再删除
            if (zipFile.exists() && !zipFile.delete()) {
                zipFile.deleteOnExit();
            }
        }
    }
}
