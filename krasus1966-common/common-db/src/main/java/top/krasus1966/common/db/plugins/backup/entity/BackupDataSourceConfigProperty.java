package top.krasus1966.common.db.plugins.backup.entity;

import lombok.Data;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author krasus1966
 * @date 2024/12/5 10:14
 **/
@Data
public class BackupDataSourceConfigProperty {
    private boolean enabled = false;
    private String backupPath;
    private List<BackupDataSourceProperty> backupDataSource;
}
