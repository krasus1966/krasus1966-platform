package top.krasus1966.common.db.plugins.backup;


import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import top.krasus1966.common.db.plugins.backup.entity.BackupDataSourceProperty;

/**
 * @author krasus1966
 * @date 2024/10/16 10:16
 **/

public interface IDatabaseBackup {

    String dbType();

    Boolean backup(String filePath, String dbName, BackupDataSourceProperty dataSourceProperty,
                   DatabaseBackupRecord record);
}
