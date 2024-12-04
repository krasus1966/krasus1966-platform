package top.krasus1966.common.db.plugins.auto_create_table.core;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author krasus1966
 * @date 2024/5/23 09:31
 **/
public class AutoCreateSQLServiceFactory {

    private static final Map<String, IAutoCreateSQLService> SERVICE_MAP = new HashMap<>();

    static {
        ServiceLoader<IAutoCreateSQLService> services = ServiceLoader.load(IAutoCreateSQLService.class);
        for (IAutoCreateSQLService service : services) {
            SERVICE_MAP.put(service.dbType(), service);
        }
    }

    public static IAutoCreateSQLService getService(String type) {
        return SERVICE_MAP.get(type);
    }
}
