package top.krasus1966.db_router.property;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2023/3/13 16:05
 **/
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class DynamicDataSourceProperties {

    private Map<String, DataSourceProperties> datasource = new LinkedHashMap<>();
}
