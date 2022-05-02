package top.krasus1966.fastdfs;

import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于fastdfs-client-java 1.29-SNAPSHOT
 *
 * @author Krasus1966
 * @date 2020/8/18 21:39
 **/
@Configuration
@ConditionalOnClass(value = {StorageClient.class, StorageServer.class})
@EnableConfigurationProperties(FastProperties.class)
@ConditionalOnProperty(name = "krasus1966.fs.fastdfs.enabled", havingValue = "true", matchIfMissing = false)
public class FastDFSAutoConfig {

    private final FastProperties fastProperties;

    public FastDFSAutoConfig(FastProperties fastProperties){
        this.fastProperties = fastProperties;
    }

    @Bean
    public FastDFSClient fastDFSClient() throws Exception {
        return new FastDFSClient(fastProperties);
    }
}
