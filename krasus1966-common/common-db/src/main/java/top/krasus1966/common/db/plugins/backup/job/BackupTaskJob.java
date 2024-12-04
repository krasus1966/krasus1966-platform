package top.krasus1966.common.db.plugins.backup.job;



import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import top.krasus1966.common.db.plugins.backup.DatabaseBackupFactory;
import top.krasus1966.common.db.plugins.backup.DatabaseBackupRecord;
import top.krasus1966.common.db.plugins.backup.IDatabaseBackup;
import top.krasus1966.common.db.plugins.backup.job.entity.BackupBaseJob;
import top.krasus1966.common.db.plugins.backup.job.entity.BackupTaskInfo;
import top.krasus1966.common.db.plugins.backup.job.entity.JobKey;
import top.krasus1966.common.db.plugins.backup.job.repository.BackupRepository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Krasus1966
 * @date 2022/5/2 16:07
 **/
@Slf4j
public class BackupTaskJob {

    private final BackupRepository repository;
    private final String backupPath;
    private final DatabaseBackupFactory databaseBackupFactory;
    private final List<DataSourceProperties> propertiesList;

    public BackupTaskJob(BackupRepository repository, DatabaseBackupFactory databaseBackupFactory, String backupPath,
                         List<DataSourceProperties> propertiesList) {
        this.repository = repository;
        this.backupPath = backupPath;
        this.databaseBackupFactory = databaseBackupFactory;
        this.propertiesList = propertiesList;
        initDir(backupPath);
    }

    public void registerJob(BackupBaseJob job) {
        boolean isRunning = running(job.getJobKey());
        String taskInfo = "";
        boolean isSucc = true;
        if (isRunning) {
            long beginTime = System.currentTimeMillis();
            try {
                taskInfo = work();
            } catch (Exception e) {
                log.error("定时任务执行失败", e);
                isSucc = false;
            }
            long endTime = System.currentTimeMillis();
            workAfter(job.getJobKey(), endTime - beginTime, isSucc, taskInfo);
        }
    }

    /**
     * 子任务执行
     *
     * @return java.lang.String
     * @method work
     * @author krasus1966
     * @date 2022/5/2 18:22
     * @description 子任务执行
     */
    public String work() {
        Set<String> alreadyDeal = new HashSet<>();
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        if (propertiesList != null && !propertiesList.isEmpty()) {
            for (DataSourceProperties dataSourceProperties : propertiesList) {
                String jdbcUrl = dataSourceProperties.getJdbcUrl();
                String hostAndDbName = DatabaseBackupFactory.getHostAndDbName(jdbcUrl);
                if (ObjectUtil.isEmpty(hostAndDbName) || alreadyDeal.contains(hostAndDbName)) {
                    continue;
                }
                String dbType = DatabaseBackupFactory.getPattern(jdbcUrl, "db");
                Date crtTime = new Date();

                // 创建备份日志
                DatabaseBackupRecord record = new DatabaseBackupRecord();
                record.setId(UUIDUtil.getUUID());
                record.setDbType(dbType);
                record.setDbName(DatabaseBackupFactory.getPattern(jdbcUrl, "dbName"));
                record.setSuccess(false);
                record.setCrtTime(crtTime);

                builder.append("连接池{").append(dataSourceProperties.getPoolName()).append("}\n数据库类型{").append(dbType).append("}\n");
                IDatabaseBackup service = databaseBackupFactory.getServiceByJdbcUrl(jdbcUrl);
                if (service == null) {
                    builder.append("ERR:不支持的数据库类型").append("\n");
                } else {
                    String file = backupPath + service.dbType() + "_" + DatabaseBackupFactory.getPattern(jdbcUrl,
                            "dbName") + "_" + sdf.format(crtTime) + ".sql";
                    Boolean isSuccess = service.backup(file, DatabaseBackupFactory.getPattern(jdbcUrl, "dbName"),
                            dataSourceProperties, record);
                    if (isSuccess) {
                        record.setFilePath(file);
                        record.setSuccess(true);
                        builder.append("备份成功!");
                    } else {
                        builder.append("备份失败!");
                    }
                }
                repository.insertRecord(record);

                alreadyDeal.add(hostAndDbName);
            }
        } else {
            builder.append("未找到数据源配置");
        }
        return builder.toString();
    }

    /***
     * 初始化备份目录
     * @param path
     * @return void
     * @throws
     * @method initDir
     * @author krasus1966
     * @date 2024/10/16
     * @description 初始化备份目录
     **/
    private void initDir(String path) {
        if (null != path && !path.isBlank()) {
            File f = new File(path);
            if (f.exists()) {
                if (f.isFile()) {
                    log.error("[{}] is a file ,not a directory !", path);
                }
            } else {
                if (!f.mkdirs()) {
                    log.error(" The directory [{}] creation failed !", path);
                }
            }
        } else {
            throw new RuntimeException("未配置备份目录！！！");
        }
    }

    /**
     * 启动任务
     *
     * @return int
     * @method running
     * @author krasus1966
     * @date 2022/5/2 18:23
     * @description 启动任务
     */
    private boolean running(JobKey jobKey) {
        try {
            // 获取任务id
            String taskId = jobKey.getName();
            // 设置任务为运行状态
            return repository.setTaskRunning(taskId);
        } catch (Exception e) {
            log.error("running", e);
        }
        return false;
    }

    /**
     * 任务执行完毕后执行
     *
     * @param cost    耗费时间
     * @param isSucc  是否成功
     * @param runInfo 返回值
     * @return void
     * @method workAfter
     * @author krasus1966
     * @date 2022/5/2 18:23
     * @description 任务执行完毕后执行
     */
    private void workAfter(JobKey jobKey, long cost, boolean isSucc, String runInfo) {
        BackupTaskInfo backupTaskInfo = repository.findById(jobKey.getName());
        if (backupTaskInfo != null) {
            try {
                int taskStatus = BackupTaskInfo.TaskStatus.TASK_SLEEP;

                Date date = new Date();
                backupTaskInfo.setLastTime(date);
                backupTaskInfo.setLastInfo(runInfo);
                backupTaskInfo.setStatus(taskStatus);
                backupTaskInfo.setRunCount((backupTaskInfo.getRunCount() == null ? 1 :
                        (backupTaskInfo.getRunCount() + 1)));
                backupTaskInfo.setLastDuration(cost);
                if (isSucc) {
                    backupTaskInfo.setLastSuccessTime(date);
                }

                boolean result = repository.updateById(backupTaskInfo);
                if (!result) {
                    log.debug("Task is not exist  (taskId = " + jobKey.getName() + ")");
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }
}
