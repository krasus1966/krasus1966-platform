package top.krasus1966.common.db.plugins.backup.job.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import top.krasus1966.common.db.plugins.backup.job.BackupTaskJob;
import top.krasus1966.common.db.plugins.backup.job.entity.BackupBaseJob;
import top.krasus1966.common.db.plugins.backup.job.entity.JobKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Krasus1966
 * @date 2023/4/19 22:03
 **/
@Slf4j
public class BackupTaskManageService {
    public final ConcurrentHashMap<JobKey, ScheduledFuture<?>> runningTask =
            new ConcurrentHashMap<>();
    public final ConcurrentHashMap<JobKey, BackupBaseJob> runningTaskJob = new ConcurrentHashMap<>();

    /**
     * 新建任务
     *
     * @param obj 任务对象
     * @method add
     * @author krasus1966
     * @date 2022/5/2 19:38
     * @description 新建任务
     */
    public void add(BackupTaskJob backupTaskJob, BackupBaseJob obj, ScheduledTaskRegistrar taskRegistrar) {
        try {
            // 构建一个触发器，规定触发的规则
            CronTrigger trigger = new CronTrigger(obj.getCron());
            if (exist(obj.getJobKey())) {
                // 存在则更新任务
                delete(obj);
                addJob(obj, backupTaskJob, trigger, taskRegistrar);
            } else {
                // 不存在则新增任务
                addJob(obj, backupTaskJob, trigger, taskRegistrar);
            }
        } catch (Exception e) {
            log.error("新建任务失败", e);
        }
    }

    private void addJob(BackupBaseJob obj, BackupTaskJob backupTaskJob, CronTrigger trigger,
                        ScheduledTaskRegistrar taskRegistrar) {
        log.debug("添加任务:{},{},{},{}",
                obj.getJobName(),
                obj.getJobGroup(),
                trigger.getExpression());
        TaskScheduler scheduler = taskRegistrar.getScheduler();
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            backupTaskJob.registerJob(obj);
        }, trigger);
        runningTask.put(obj.getJobKey(), future);
        runningTaskJob.put(obj.getJobKey(), obj);
    }

    /**
     * 删除任务
     *
     * @param obj 任务对象
     * @method delete
     * @author krasus1966
     * @date 2022/5/2 19:34
     * @description 删除任务
     */
    public void delete(BackupBaseJob obj) {
        delete(obj.getJobKey());
    }

    /**
     * 删除任务
     *
     * @param jobKey key,value对象
     * @return boolean
     * @method delete
     * @author krasus1966
     * @date 2022/5/2 19:33
     * @description 删除任务
     */
    public boolean delete(JobKey jobKey) {
        ScheduledFuture<?> remove = runningTask.remove(jobKey);
        if (remove != null) {
            if (remove.cancel(true)) {
                runningTaskJob.remove(jobKey);
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在特定的任务
     *
     * @param obj 任务对象
     * @return boolean
     * @method exist
     * @author krasus1966
     * @date 2022/5/2 19:33
     * @description 是否存在特定的任务
     */
    public boolean exist(BackupBaseJob obj) {
        return exist(obj.getJobKey());
    }

    /**
     * 是否存在特定的任务
     *
     * @param jobKey key,value
     * @return boolean
     * @method exist
     * @author krasus1966
     * @date 2022/5/2 19:32
     * @description 是否存在特定的任务
     */
    public boolean exist(JobKey jobKey) {
        return runningTask.containsKey(jobKey);
    }

    /**
     * 清除所有的任务
     *
     * @method clear
     * @author krasus1966
     * @date 2022/5/2 19:32
     * @description 清除所有的任务
     */
    public void clear() {
        for (Map.Entry<JobKey, ScheduledFuture<?>> job :
                runningTask.entrySet()) {
            job.getValue().cancel(true);
        }
        runningTask.clear();
        runningTaskJob.clear();
    }

    /**
     * 获取任务
     *
     * @param name  job名称
     * @param group job分组
     * @return top.krasus1966.quartz.entity.BaseJob
     * @method getJob
     * @author krasus1966
     * @date 2022/5/2 19:31
     * @description 获取任务
     */
    public BackupBaseJob getJob(String name, String group) {
        JobKey jobKey = new JobKey(name, group);
        if (!exist(jobKey)) {
            return null;
        }
        return runningTaskJob.get(jobKey);
    }
}
