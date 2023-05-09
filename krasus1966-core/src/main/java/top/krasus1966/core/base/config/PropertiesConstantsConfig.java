package top.krasus1966.core.base.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import top.krasus1966.core.base.constant.ConvertProperty;
import top.krasus1966.core.base.constant.LoginConstants;

/**
 * 配置注入自定义常量
 *
 * @author Krasus1966
 * @date 2022/10/31 16:47
 **/
@EnableConfigurationProperties({LoginConstants.class, ConvertProperty.class})
@Component
public class PropertiesConstantsConfig {
}
