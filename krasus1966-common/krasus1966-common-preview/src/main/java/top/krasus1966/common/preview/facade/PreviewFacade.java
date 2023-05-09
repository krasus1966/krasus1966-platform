package top.krasus1966.common.preview.facade;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.common.file.entity.dto.FileInfoDTO;
import top.krasus1966.common.file.service.IFileService;
import top.krasus1966.common.preview.entity.FileAttribute;
import top.krasus1966.common.preview.entity.FilePreviewType;
import top.krasus1966.common.preview.entity.FileType;
import top.krasus1966.common.preview.exception.ConvertException;
import top.krasus1966.common.preview.factory.PreviewFactory;
import top.krasus1966.common.preview.service.FilePreviewService;
import top.krasus1966.core.base.constant.ConvertProperty;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.facade.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Krasus1966
 * @date 2023/5/6 23:55
 **/
@RestController
@RequestMapping("/file/preview")
@Slf4j
public class PreviewFacade extends BaseController {

    private static final String TEMP_PATH_NAME = "preview";
    private final IFileService fileService;
    private final PreviewFactory previewFactory;
    private final ConvertProperty convertProperty;

    public PreviewFacade(HttpServletRequest request, HttpServletResponse response,
                         IFileService fileService,
                         PreviewFactory previewFactory,
                         ConvertProperty convertProperty) {
        super(request, response);
        this.fileService = fileService;
        this.previewFactory = previewFactory;
        this.convertProperty = convertProperty;
    }

    @RequestMapping("/{fileId}")
    public R<?> preview(@PathVariable String fileId,@RequestParam(required = false) String previewType) throws IOException {
        // 通过fileId获取文件信息
        FileInfoDTO fileInfo = fileService.getOneById(fileId);
        if (null == fileInfo) {
            return R.failed("文件不存在");
        }
        fileInfo.setInputStream(null);
        // 检查
        String suffix = FileUtil.getSuffix(fileInfo.getFileName()).toLowerCase(Locale.ROOT);
        if (null != previewType && !FilePreviewType.checkType(suffix, previewType)) {
            throw new ConvertException("当前文件不能使用您选择的预览方式");
        }
        String url = convertProperty.getDownloadUrl() + fileId;
        FileAttribute fileAttribute = new FileAttribute();
        fileAttribute.setFileId(fileId);
        fileAttribute.setFileName(FileUtil.getPrefix(fileInfo.getFileName()));
        fileAttribute.setFileUrl(url);
        fileAttribute.setDownloadUrl(convertProperty.getDownloadUrl());
        fileAttribute.setFileType(FileType.typeFromFileName(fileInfo.getFileName()));
        fileAttribute.setSuffix(suffix);
        fileAttribute.setMd5(fileInfo.getMd5());
        fileAttribute.setOutputPath(convertProperty.getTempFilePath() + TEMP_PATH_NAME + File.separator);
        fileAttribute.setPreviewType(previewType);
        FilePreviewService filePreviewService = previewFactory.get(fileAttribute);
        if (null == filePreviewService) {
            return R.failed("暂不支持对应文件格式的预览");
        }
        return R.success(filePreviewService.filePreviewHandle(fileAttribute,fileInfo));
    }
}
