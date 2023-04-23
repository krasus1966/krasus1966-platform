package top.krasus1966.db_router;

/**
 * @author Krasus1966
 * @date 2023/3/13 15:54
 **/
public class DynamicDataSourceContext {

    private ThreadLocal<String> dataSourceName = new ThreadLocal<>();

    public ThreadLocal<String> getDataSourceName() {
        return dataSourceName;
    }

    public DynamicDataSourceContext setDataSourceName(ThreadLocal<String> dataSourceName) {
        this.dataSourceName = dataSourceName;
        return this;
    }
}
