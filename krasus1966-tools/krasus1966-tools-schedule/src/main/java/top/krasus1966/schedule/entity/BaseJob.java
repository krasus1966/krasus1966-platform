package top.krasus1966.schedule.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import top.krasus1966.schedule.config.JacksonEnum;

import java.util.Date;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2022/5/2 16:35
 **/
public class BaseJob {

    public static final Logger log = LoggerFactory.getLogger(BaseJob.class);

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
     * 任务类
     */
    private String className;
    /**
     * 参数
     */
    private String dataMap;
    private Map<String,String> realDataMap;
    /**
     * 描述
     */
    private String comment;
    /**
     * 开始执行时间，早于此时间不执行
     */
    private Date beginTime;
    /**
     * 结束执行时间，晚于此时间不执行
     */
    private Date endTime;
    /**
     * 触发器
     */
    private Trigger trigger;

    public BaseJob() {
    }

    public BaseJob(JobKey jobKey, Trigger trigger,String className, Map<String, String> dataMap, Date beginTime, Date endTime) {
        setJobKey(jobKey);
        setTrigger(trigger);
        this.className = className;
        try {
            if (!dataMap.isEmpty()) {
                this.dataMap = JacksonEnum.INSTANCE.getObjectMapper().writeValueAsString(dataMap);
            }
        } catch (JsonProcessingException e) {
            log.error("BaseJob Json转换失败", e);
        }
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    /**
     * 通过数据库任务对象获取真实任务对象
     *
     * @param taskInfo 任务对象
     * @return top.krasus1966.quartz.entity.BaseJob
     * @method getJobByTaskInfo
     * @author krasus1966
     * @date 2022/5/2 23:17
     * @description 通过数据库任务对象获取真实任务对象
     */
    public BaseJob getJobByTaskInfo(TaskInfo taskInfo) {
        this.jobName = taskInfo.getId();
        this.jobGroup = taskInfo.getId();
        this.className = taskInfo.getRunClazz();
        this.cron = taskInfo.getPolicy();
        this.comment = taskInfo.getTaskName();
        this.dataMap = taskInfo.getParam();
        this.beginTime = taskInfo.getBeginTime();
        this.endTime = taskInfo.getEndTime();
        return this;
    }

    /**
     * 判断任务是否改变
     *
     * @param taskInfo 任务对象
     * @return boolean
     * @method isChange
     * @author krasus1966
     * @date 2022/5/2 23:16
     * @description 判断任务是否改变
     */
    public boolean isChange(TaskInfo taskInfo) {
        // 类改变
        if (!CharSequenceUtil.trimToEmpty(this.className).equals(CharSequenceUtil.trimToEmpty(taskInfo.getRunClazz()))) {
            return true;
        }
        // 表达式改变
        if (!CharSequenceUtil.trimToEmpty(this.cron).equals(CharSequenceUtil.trimToEmpty(taskInfo.getPolicy()))) {
            return true;
        }
        // 参数改变
        if (!CharSequenceUtil.trimToEmpty(this.dataMap).equals(CharSequenceUtil.trimToEmpty(taskInfo.getParam()))) {
            return true;
        }
        // 启动时间改变，只关注新时间是否晚于当前时间
        if (null != this.beginTime && null != taskInfo.getBeginTime() && this.beginTime.before(taskInfo.getBeginTime())) {
            return true;
        }
        // 结束时间改变
        if (ObjectUtil.notEqual(this.endTime, taskInfo.getEndTime())) {
            return true;
        }
        return false;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobName = jobKey.getName();
        this.jobGroup = jobKey.getGroup();
    }

    public JobKey getJobKey() {
        return new JobKey(jobName, jobGroup);
    }

    public String getId() {
        return id;
    }

    public BaseJob setId(String id) {
        this.id = id;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public BaseJob setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public BaseJob setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
        return this;
    }

    public String getCron() {
        return cron;
    }

    public BaseJob setCron(String cron) {
        this.cron = cron;
        return this;
    }

    public String getScheduled() {
        return scheduled;
    }

    public BaseJob setScheduled(String scheduled) {
        this.scheduled = scheduled;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public BaseJob setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getDataMap() {
        return dataMap;
    }

    public BaseJob setDataMap(String dataMap) {
        this.dataMap = dataMap;
        return this;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public BaseJob setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public BaseJob setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public BaseJob setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
        if (trigger instanceof CronTrigger) {
            cron = ((CronTrigger) trigger).getExpression();
        }
    }

    public Map<String, String> getRealDataMap() {
        return realDataMap;
    }

    public BaseJob setRealDataMap(Map<String, String> realDataMap) {
        this.realDataMap = realDataMap;
        return this;
    }
}
