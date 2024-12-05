package top.krasus1966.common.db.plugins.backup.entity;


/**
 * @author krasus1966
 * @date 2024/12/5 10:12
 **/
public record BackupDataSourceProperty(String dbType,String dataSourceName,String url,String username,
                                       String password) {}
