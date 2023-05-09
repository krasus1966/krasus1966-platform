package top.krasus1966.common.preview.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Krasus1966
 * @date 2023/5/6 23:03
 **/
@Data
@ConfigurationProperties(prefix = "project.office")
public class OfficeProperty {

    /**
     * office位置
     */
    private String officeHome;

    /**
     * office转换服务监听端口号，多个逗号分割
     */
    private String serverPorts = "9792";

    /**
     * 转换超时时间
     */
    private String convertTimeout = "5m";


}
