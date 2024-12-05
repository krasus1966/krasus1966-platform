package top.krasus1966.common.db.plugins.backup;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import top.krasus1966.common.db.plugins.backup.entity.BackupDataSourceProperty;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author krasus1966
 * @date 2024/10/16 10:16
 **/
@Slf4j
public class DatabaseBackupFactory {
    private static final Pattern jdbcUrlPattern = Pattern.compile("jdbc:(?<db>\\w+):.*((//)|@)(?<host>.+):" +
            "(?<port>\\d+)" + "(/|(;" + "DatabaseName=)|:)" + "(?<dbName>\\w+.+)\\?");
    private final Map<String, IDatabaseBackup> backupService;

    public DatabaseBackupFactory(ApplicationContext applicationContext) {
        Map<String, IDatabaseBackup> beansOfType = applicationContext.getBeansOfType(IDatabaseBackup.class);
        Map<String, IDatabaseBackup> backupMap = new HashMap<>();
        for (IDatabaseBackup databaseBackup : beansOfType.values()) {
            backupMap.put(databaseBackup.dbType().toLowerCase(Locale.ROOT), databaseBackup);
        }
        this.backupService = backupMap;
    }

    public static String getPattern(String url, String match) {
        Matcher m = jdbcUrlPattern.matcher(url);
        if (m.find()) {
            return m.group(match);
        }
        return null;
    }

    public static String getHostAndDbName(String url) {
        Matcher m = jdbcUrlPattern.matcher(url);
        if (m.find()) {
            return m.group("db") + ":" + m.group("host") + ":" + m.group("port") + ":" + m.group("dbName");
        }
        return null;
    }

    public IDatabaseBackup getService(String dbType) {
        if (!backupService.containsKey(dbType.toLowerCase(Locale.ROOT))) {
            log.warn("暂不支持【{}】类型数据库", dbType);
            return null;
        }
        return backupService.get(dbType);
    }

    public IDatabaseBackup getServiceByJdbcUrl(String jdbcUrl) {
        String dbType = getPattern(jdbcUrl, "db");
        if (CharSequenceUtil.isEmpty(dbType)) {
            throw new RuntimeException("读取数据库类型失败");
        }
        return getService(dbType.toLowerCase(Locale.ROOT));
    }

    public IDatabaseBackup getService(BackupDataSourceProperty dataSourceProperties) {
        String dbType = getPattern(dataSourceProperties.url(), "db");
        if (CharSequenceUtil.isEmpty(dbType)) {
            throw new RuntimeException("读取数据库类型失败");
        }
        return getService(dbType.toLowerCase(Locale.ROOT));
    }
}
