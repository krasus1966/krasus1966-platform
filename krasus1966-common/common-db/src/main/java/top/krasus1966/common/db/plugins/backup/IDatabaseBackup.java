package top.krasus1966.common.db.plugins.backup;


import com.ttsx.common.base.entity.DataSourceProperties;

/**
 * @author krasus1966
 * @date 2024/10/16 10:16
 **/

public interface IDatabaseBackup {

    String dbType();

    Boolean backup(String filePath, String dbName, DataSourceProperties dataSourceProperties,
                   DatabaseBackupRecord record);
}
