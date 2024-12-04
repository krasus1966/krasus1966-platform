package top.krasus1966.common.db.plugins.backup.impl;

import com.ttsx.common.base.backup.DatabaseBackupFactory;
import com.ttsx.common.base.backup.DatabaseBackupRecord;
import com.ttsx.common.base.backup.IDatabaseBackup;
import com.ttsx.common.base.entity.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @author krasus1966
 * @date 2024/10/16 10:21
 **/
@Service
@Slf4j
public class MySQLDatabaseBackup implements IDatabaseBackup {
    @Override
    public String dbType() {
        return "MySQL";
    }

    @Override
    public Boolean backup(String fileName, String dbName, DataSourceProperties dataSourceProperties,
                          DatabaseBackupRecord record) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("mysqldump", "-h",
                    DatabaseBackupFactory.getPattern(dataSourceProperties.getJdbcUrl(), "host"),
                    "-P", DatabaseBackupFactory.getPattern(dataSourceProperties.getJdbcUrl(), "port"),
                    "-u", dataSourceProperties.getUsername(), dbName);
            processBuilder.environment().put("MYSQL_PWD", dataSourceProperties.getPassword());
            processBuilder.redirectOutput(new File(fileName));
            Process process = processBuilder.start();

            int exitCode;
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String errorLine;
                StringBuilder err = new StringBuilder();
                while ((errorLine = errorReader.readLine()) != null) {
                    log.error("MySQL Error during backup: {}", errorLine);
                    err.append(errorLine);
                }
                record.setErrLog(err.toString());
                exitCode = process.waitFor();
            }
            if (exitCode == 0) {
                log.info("MySQL backup successful.");
                return true;
            } else {
                log.error("MySQL backup failed.");
                return false;
            }
        } catch (Exception e) {
            log.error("MySQL backup failed.", e);
            return false;
        }
    }
}
