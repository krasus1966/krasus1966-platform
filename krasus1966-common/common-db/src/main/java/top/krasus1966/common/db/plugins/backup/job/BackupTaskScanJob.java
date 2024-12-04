package top.krasus1966.common.db.plugins.backup.job;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import top.krasus1966.common.db.plugins.backup.DatabaseBackupFactory;
import top.krasus1966.common.db.plugins.backup.job.entity.BackupBaseJob;
import top.krasus1966.common.db.plugins.backup.job.entity.BackupTaskInfo;
import top.krasus1966.common.db.plugins.backup.job.entity.JobKey;
import top.krasus1966.common.db.plugins.backup.job.repository.BackupRepository;
import top.krasus1966.common.db.plugins.backup.job.service.BackupTaskManageService;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/4/19 22:22
 **/
@Slf4j
public class BackupTaskScanJob implements SchedulingConfigurer {

    private final BackupTaskManageService manageService;
    private final BackupRepository repository;
    private final BackupTaskJob backupTaskJob;

    public BackupTaskScanJob(JdbcTemplate jdbcTemplate, DatabaseBackupFactory databaseBackupFactory,
                             BackupProperties backupProperties, List<DataSourceProperties> propertiesList) {
        this.manageService = new BackupTaskManageService();
        this.repository = new BackupRepository(jdbcTemplate);
        this.backupTaskJob = new BackupTaskJob(repository, databaseBackupFactory, backupProperties.getBackupPath(),
                propertiesList);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(new CronTask(() -> {
            try {
                log.debug("开始扫描任务，线程:{}", Thread.currentThread().getName());
                //获取全部任务
                List<BackupTaskInfo> backupTaskInfoList = repository.findAll();
                if (backupTaskInfoList != null && !backupTaskInfoList.isEmpty()) {
                    for (BackupTaskInfo backupTaskInfo : backupTaskInfoList) {
                        checkTaskInfo(backupTaskInfo, taskRegistrar);
                    }
                }
            } catch (Exception e) {
                log.error("加载任务列表失败", e);
            }
            log.debug("扫描完成，线程:{}", Thread.currentThread().getName());
        }, "0/10 * * * * ?"));
    }

    /**
     * 检查并处理任务
     *
     * @param backupTaskInfo 任务对象
     * @method checkTaskInfo
     * @author krasus1966
     * @date 2022/5/2 23:29
     * @description 检查并处理任务
     */
    private void checkTaskInfo(BackupTaskInfo backupTaskInfo, ScheduledTaskRegistrar taskRegistrar) {
        //判断任务是否在当前任务管理器中
        boolean isExist = manageService.exist(new JobKey(backupTaskInfo.getId(), backupTaskInfo.getId()));
        // 如果存在
        if (isExist) {
            //从任务管理器中获取这个任务
            BackupBaseJob backupBaseJob = manageService.getJob(backupTaskInfo.getId(), backupTaskInfo.getId());
            //判断当前表中任务是否被删除了
            if (backupTaskInfo.isDel()) {
                //如果表中删除了，则任务管理器中也要删除
                log.debug("任务已被删除:{}", backupTaskInfo.getTaskName());
                manageService.delete(backupBaseJob);
            } else if (backupTaskInfo.isStop()) {
                // 如果表中停止，则任务管理器中删除
                log.debug("任务手动结束:{}", backupTaskInfo.getTaskName());
                manageService.delete(backupBaseJob);
            } else {
                //如果表中没有删除，判断任务管理器中的任务与数据库中任务是否关键字段是否改变
                //如果改变了，那么就重新将人无添加到任务管理器中
                if (backupBaseJob.isChange(backupTaskInfo)) {
                    //任务是否覆盖？？？有争议
                    manageService.add(backupTaskJob, backupBaseJob.getJobByTaskInfo(backupTaskInfo), taskRegistrar);
                }
            }
        } else { // 不存在 时 如果任务没有被删除，并且不是待执行
            if (!backupTaskInfo.isDel()) {
                if (BackupTaskInfo.TaskStatus.TASK_WAITING.equals(backupTaskInfo.getStatus()) || BackupTaskInfo.TaskStatus.TASK_RUNNING.equals(backupTaskInfo.getStatus()) || BackupTaskInfo.TaskStatus.TASK_SLEEP.equals(backupTaskInfo.getStatus())) {
                    BackupBaseJob backupBaseJob = new BackupBaseJob();
                    manageService.add(backupTaskJob, backupBaseJob.getJobByTaskInfo(backupTaskInfo), taskRegistrar);
                }
            }
        }
    }
}
