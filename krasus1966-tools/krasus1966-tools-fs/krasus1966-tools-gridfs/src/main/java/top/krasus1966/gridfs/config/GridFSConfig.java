package top.krasus1966.gridfs.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import top.krasus1966.common.file.service.*;
import top.krasus1966.core.base.constant.ConvertConstants;
import top.krasus1966.gridfs.service.MongoFileChunkServiceImpl;
import top.krasus1966.gridfs.service.MongoFileQueryServiceImpl;
import top.krasus1966.gridfs.service.MongoFileStoreServiceImpl;


/**
 * @author Krasus1966
 * {@code @date} 2023/4/4 23:51
 **/
@Configuration
public class GridFSConfig {
    @Bean
    @ConditionalOnMissingBean(IFileStoreService.class)
    public IFileStoreService fileStoreService(GridFsTemplate gridFsTemplate) {
        return new MongoFileStoreServiceImpl(gridFsTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(IFileQueryService.class)
    public IFileQueryService fileQueryService(GridFsTemplate gridFsTemplate) {
        return new MongoFileQueryServiceImpl(gridFsTemplate);
    }


    @Bean
    @ConditionalOnMissingBean(IFileChunkService.class)
    public IFileChunkService fileChunkService(GridFsTemplate gridFsTemplate, ConvertConstants convertConstants) {
        return new MongoFileChunkServiceImpl(gridFsTemplate,convertConstants);
    }

    @Bean
    @ConditionalOnMissingBean(IFileService.class)
    @DependsOn({"fileStoreService", "fileQueryService", "fileChunkService"})
    public IFileService fileService(IFileStoreService fileStoreService,
                                    IFileQueryService fileQueryService,
                                    IFileChunkService fileChunkService) {
        return new FileServiceImpl(fileStoreService, fileQueryService, fileChunkService);
    }
}
