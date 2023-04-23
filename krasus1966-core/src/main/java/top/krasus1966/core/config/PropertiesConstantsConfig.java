package top.krasus1966.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import top.krasus1966.core.constant.PropertiesConstants;

/**
 * 配置注入自定义常量
 *
 * @author Krasus1966
 * @date 2022/10/31 16:47
 **/
@EnableConfigurationProperties(PropertiesConstants.class)
@Component
public class PropertiesConstantsConfig {
}
