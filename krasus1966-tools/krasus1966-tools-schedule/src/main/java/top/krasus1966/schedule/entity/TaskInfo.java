package top.krasus1966.schedule.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * @author Krasus1966
 * @date 2022/5/2 16:43
 **/
@Table(name = "sys_task",
    indexes = {
        @Index(name = "idx_deleted",columnList = "deleted",unique = false),
        @Index(name = "idx_status",columnList = "status",unique = false)
    }
)
@Entity
public class TaskInfo {

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

    /**
     * id
     */
    @Id
    private String id;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 执行类
     */
    private String runClazz;
    /**
     * 参数
     */
    private String param;
    /**
     * 开始执行时间，早于此时间不执行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    /**
     * 结束执行时间，晚于此时间不执行
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
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


    public String getId() {
        return id;
    }

    public TaskInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public TaskInfo setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String getRunClazz() {
        return runClazz;
    }

    public TaskInfo setRunClazz(String runClazz) {
        this.runClazz = runClazz;
        return this;
    }

    public String getParam() {
        return param;
    }

    public TaskInfo setParam(String param) {
        this.param = param;
        return this;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public TaskInfo setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public TaskInfo setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getPolicy() {
        return policy;
    }

    public TaskInfo setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public TaskInfo setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public TaskInfo setNextTime(Date nextTime) {
        this.nextTime = nextTime;
        return this;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public TaskInfo setLastTime(Date lastTime) {
        this.lastTime = lastTime;
        return this;
    }

    public Long getLastDuration() {
        return lastDuration;
    }

    public TaskInfo setLastDuration(Long lastDuration) {
        this.lastDuration = lastDuration;
        return this;
    }

    public Date getLastSuccessTime() {
        return lastSuccessTime;
    }

    public TaskInfo setLastSuccessTime(Date lastSuccessTime) {
        this.lastSuccessTime = lastSuccessTime;
        return this;
    }

    public Integer getRunCount() {
        return runCount;
    }

    public TaskInfo setRunCount(Integer runCount) {
        this.runCount = runCount;
        return this;
    }

    public String getLastInfo() {
        return lastInfo;
    }

    public TaskInfo setLastInfo(String lastInfo) {
        this.lastInfo = lastInfo;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public TaskInfo setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "id='" + id + '\'' +
                ", taskName='" + taskName + '\'' +
                ", runClazz='" + runClazz + '\'' +
                ", param='" + param + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", policy='" + policy + '\'' +
                ", status=" + status +
                ", nextTime=" + nextTime +
                ", lastTime=" + lastTime +
                ", lastDuration=" + lastDuration +
                ", lastSuccessTime=" + lastSuccessTime +
                ", runCount=" + runCount +
                ", lastInfo='" + lastInfo + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
