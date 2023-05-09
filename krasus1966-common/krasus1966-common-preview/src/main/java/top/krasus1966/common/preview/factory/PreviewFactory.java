package top.krasus1966.common.preview.factory;

import org.springframework.stereotype.Service;
import top.krasus1966.common.preview.entity.FileAttribute;
import top.krasus1966.common.preview.service.FilePreviewService;
import top.krasus1966.core.spring.util.SpringUtil;

import java.util.Map;

/**
 * @author Krasus1966
 * @date 2023/5/7 00:06
 **/
@Service
public class PreviewFactory {

    public FilePreviewService get(FileAttribute fileAttribute) {
        Map<String, FilePreviewService> filePreviewMap = SpringUtil.getApplicationContext().getBeansOfType(FilePreviewService.class);
        return filePreviewMap.get(fileAttribute.getFileType().getInstanceName());
    }
}
