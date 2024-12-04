package top.krasus1966.common.db.plugins.backup.job.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Krasus1966
 * @date 2022/5/2 16:43
 **/
@Data
public class BackupTaskInfo {

    /**
     * id
     */
    private String id;
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 执行策略，cron表达式
     */
    private String policy;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 下次执行时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextTime;
    /**
     * 上一次执行时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastTime;
    /**
     * 上一次执行耗时
     */
    private Long lastDuration;
    /**
     * 上一次执行成功时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastSuccessTime;
    /**
     * 执行次数
     */
    private Integer runCount;
    /**
     * 任务返回信息
     */
    private String lastInfo;
    /**
     * 任务是否删除
     */
    private Integer deleted;

    /**
     * 判断任务是否终止
     *
     * @return boolean
     * @method isStop
     * @author krasus1966
     * @date 2022/5/2 16:49
     * @description 判断任务是否终止
     */
    public boolean isStop() {
        return TaskStatus.TASK_FINISH.equals(this.status) || TaskStatus.TASK_END.equals(this.status);
    }

    /**
     * 判断任务是否删除
     *
     * @return boolean
     * @method isDel
     * @author krasus1966
     * @date 2022/5/2 16:48
     * @description 判断任务是否删除
     */
    public boolean isDel() {
        return TaskDeleteStatus.TASK_DELETED.equals(this.deleted);
    }

    public interface TaskStatus {
        Integer TASK_WAITING = 0;
        Integer TASK_RUNNING = 1;
        Integer TASK_SLEEP = 2;
        Integer TASK_STOP = 3;
        Integer TASK_FINISH = 4;
        Integer TASK_END = 5;
    }

    public interface TaskDeleteStatus {
        Integer TASK_NOT_DELETE = 0;
        Integer TASK_DELETED = 1;
    }
}
