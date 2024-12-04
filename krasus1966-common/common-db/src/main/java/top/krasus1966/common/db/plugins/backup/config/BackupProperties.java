package top.krasus1966.common.db.plugins.backup.config;

import com.ttsx.common.base.entity.DataSourceProperties;
import lombok.Data;


/**
 * @author krasus1966
 * @date 2024/10/16 11:07
 **/
@Data
public class BackupProperties {
    private boolean enabled = false;
    private String backupPath;
    private DataSourceProperties db;
}
