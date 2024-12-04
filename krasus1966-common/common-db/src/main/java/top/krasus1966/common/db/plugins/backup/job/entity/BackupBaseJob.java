package top.krasus1966.common.db.plugins.backup.job.entity;

import lombok.Data;
import org.springframework.scheduling.Trigger;

/**
 * @author Krasus1966
 * @date 2022/5/2 16:35
 **/
@Data
public class BackupBaseJob {

    private String id;
    /**
     * job名
     */
    private String jobName;
    /**
     * job分组名
     */
    private String jobGroup;
    /**
     * 表达式
     */
    private String cron;
    /**
     * 调度器
     */
    private String scheduled;

    /**
     * 描述
     */
    private String comment;

    /**
     * 触发器
     */
    private Trigger trigger;

    public BackupBaseJob() {
    }

    /**
     * 通过数据库任务对象获取真实任务对象
     *
     * @param backupTaskInfo 任务对象
     * @return top.krasus1966.quartz.entity.BaseJob
     * @method getJobByTaskInfo
     * @author krasus1966
     * @date 2022/5/2 23:17
     * @description 通过数据库任务对象获取真实任务对象
     */
    public BackupBaseJob getJobByTaskInfo(BackupTaskInfo backupTaskInfo) {
        this.jobName = backupTaskInfo.getId();
        this.jobGroup = backupTaskInfo.getId();
        this.cron = backupTaskInfo.getPolicy();
        this.comment = backupTaskInfo.getTaskName();
        return this;
    }

    /**
     * 判断任务是否改变
     *
     * @param backupTaskInfo 任务对象
     * @return boolean
     * @method isChange
     * @author krasus1966
     * @date 2022/5/2 23:16
     * @description 判断任务是否改变
     */
    public boolean isChange(BackupTaskInfo backupTaskInfo) {
        // 表达式改变
        if (!this.cron.equals(backupTaskInfo.getPolicy())) {
            return true;
        }
        return false;
    }

    public JobKey getJobKey() {
        return new JobKey(jobName, jobGroup);
    }
}
