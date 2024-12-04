package top.krasus1966.common.db.plugins.backup.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import top.krasus1966.common.db.plugins.backup.DatabaseBackupFactory;
import top.krasus1966.common.db.plugins.backup.job.BackupTaskScanJob;

import javax.sql.DataSource;
import java.util.List;

/**
 * 用户中心数据源配置类
 *
 * @author daijiaqi
 * @version 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "backup", name = "enabled", havingValue = "true")
@EnableScheduling
@Slf4j
public class BackupDataSourceAutoConfiguration {

    public BackupDataSourceAutoConfiguration() {
        log.info("定时备份数据源已启用！");
    }

    @Bean(value = "backupTaskScheduler")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        return scheduler;
    }

    @Bean
    public BackupTaskScanJob backupTaskScanJob(DataSource dataSource,
                                               BackupProperties backupProperties,
                                               DatabaseBackupFactory databaseBackupFactory,
                                               List<DataSourceProperties> dataSourcePropertiesList) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return new BackupTaskScanJob(jdbcTemplate, databaseBackupFactory, backupProperties, dataSourcePropertiesList);
    }
}
