package top.krasus1966.common.db.plugins.backup;

import lombok.Data;

import java.util.Date;

/**
 * @author krasus1966
 * @date 2024/10/16 16:51
 **/
@Data
public class DatabaseBackupRecord {
    private String id;
    private String dbType;
    private String dbName;
    private String filePath;
    private String errLog;
    private Boolean success;
    private Date crtTime;
}
