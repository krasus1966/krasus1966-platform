package top.krasus1966.quartz.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.krasus1966.quartz.config.JacksonEnum;
import top.krasus1966.quartz.entity.BaseJob;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 定时任务管理Service
 *
 * @author Krasus1966
 * @date 2022/5/2 19:06
 **/
@Service
public class TaskManageService {

    public static final Logger log = LoggerFactory.getLogger(TaskManageService.class);

    private final Scheduler scheduler;

    private final ObjectMapper objectMapper;

    public TaskManageService(@Qualifier("scheduler") Scheduler scheduler) {
        this.scheduler = scheduler;
        this.objectMapper = JacksonEnum.INSTANCE.getObjectMapper();
    }

    /**
     * 新建任务
     *
     * @param jobName     job名称
     * @param jobGroup    job分组名称
     * @param jobParamMap job参数
     * @param cron        cron表达式
     * @param klass       定时任务类
     * @throws SchedulerException 定时任务异常
     * @method add
     * @author krasus1966
     * @date 2022/5/2 19:39
     * @description 新建任务
     */
    public void add(String jobName, String jobGroup, JobDataMap jobParamMap, String cron, Class<? extends Job> klass) throws SchedulerException {
        add(jobName, jobGroup, jobName, jobGroup, jobParamMap, null, null, klass, cron);
    }

    /**
     * 新建任务
     *
     * @param jobName     job名称
     * @param jobGroup    job分组名称
     * @param jobParamMap job参数
     * @param beginTime   任务开始时间
     * @param endTime     任务结束时间
     * @param cron        cron表达式
     * @param klass       定时任务类
     * @throws SchedulerException 定时任务异常
     * @method add
     * @author krasus1966
     * @date 2022/5/2 22:42
     * @description 新建任务
     */
    public void add(String jobName, String jobGroup, JobDataMap jobParamMap, Date beginTime, Date endTime, String cron, Class<? extends Job> klass) throws SchedulerException {
        add(jobName, jobGroup, jobName, jobGroup, jobParamMap, beginTime, endTime, klass, cron);
    }

    /**
     * 新建任务
     *
     * @param obj 任务对象
     * @method add
     * @author krasus1966
     * @date 2022/5/2 19:38
     * @description 新建任务
     */
    public void add(BaseJob obj) {
        try {
            Class<? extends Job> klass = (Class<? extends Job>) Class.forName(obj.getClassName());
            Map<String, String> tmp = null;
            if (!Strings.isBlank(obj.getDataMap())) {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(String.class, String.class);
                tmp = objectMapper.readValue(obj.getDataMap(), javaType);
            }
            JobDataMap data = tmp == null ? new JobDataMap() : new JobDataMap(tmp);
            add(obj.getJobName(), obj.getJobGroup(), obj.getJobName(), obj.getJobGroup(), data, obj.getBeginTime(), obj.getEndTime(), klass, obj.getCron());
        } catch (Exception e) {
            log.error("新建任务失败", e);
        }
    }

    /**
     * 新建任务
     *
     * @param jobName          job名称
     * @param jobGroupName     job分组名称
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器分组名称
     * @param cls              定时任务类
     * @param cron             cron表达式
     * @param beginTime        任务开始时间
     * @param endTime          任务结束时间
     * @throws SchedulerException 定时任务异常
     * @method add
     * @author krasus1966
     * @date 2022/5/2 19:37
     * @description 新建任务
     */
    public void add(String jobName,
                    String jobGroupName,
                    String triggerName,
                    String triggerGroupName,
                    JobDataMap jobParamMap,
                    Date beginTime,
                    Date endTime,
                    Class<? extends Job> cls,
                    String cron)
            throws SchedulerException {
        // 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
        JobDetail jobDetail = JobBuilder.newJob(cls)
                .withIdentity(jobName, jobGroupName)
                .setJobData(jobParamMap)
                .build();
        // 构建一个触发器，规定触发的规则
        // 创建一个新的TriggerBuilder来规范一个触发器
        CronTrigger trigger = TriggerBuilder.newTrigger()
                // 给触发器起一个名字和组名
                .withIdentity(triggerName, triggerGroupName)
                // 触发器的执行时间
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                // 产生触发器
                .build();
        if (null == beginTime) {
            // 未设置开始时间
            trigger = trigger.getTriggerBuilder().startNow().build();
        } else {
            // 设置了开始时间
            trigger = trigger.getTriggerBuilder().startAt(beginTime).build();
        }
        if (null != endTime) {
            // 设置了结束时间
            trigger = trigger.getTriggerBuilder().endAt(endTime).build();
        }
        if (exist(new JobKey(jobName, jobGroupName))) {
            // 存在则更新任务
            flush(jobDetail, trigger);
        } else {
            // 不存在则新增任务
            add(jobDetail, trigger);
        }
    }

    /**
     * 添加新任务
     *
     * @param jobDetail 任务详情
     * @param trigger   触发器
     * @throws SchedulerException 定时任务异常
     * @method add
     * @author krasus1966
     * @date 2022/5/2 21:43
     * @description 添加新任务
     */
    public void add(JobDetail jobDetail,
                    CronTrigger trigger)
            throws SchedulerException {
        scheduler.scheduleJob(jobDetail, trigger);
        log.error("添加任务:{},{},{},{},{},{}",
                jobDetail.getKey().getName(),
                jobDetail.getKey().getGroup(),
                trigger.getJobKey().getName(),
                trigger.getJobKey().getGroup(),
                jobDetail.getJobClass().getName(),
                trigger.getCronExpression());
        // 启动
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    /**
     * 更新任务
     *
     * @param jobDetail 任务详情
     * @param trigger   触发器
     * @throws SchedulerException 定时任务异常
     * @method flush
     * @author krasus1966
     * @date 2022/5/2 21:49
     * @description 更新任务
     */
    public void flush(JobDetail jobDetail,
                      CronTrigger trigger)
            throws SchedulerException {
        Set<Trigger> triggers = new HashSet<>();
        triggers.add(trigger);
        scheduler.scheduleJob(jobDetail, triggers, true);
        log.error("更新任务:{},{},{},{},{},{}",
                jobDetail.getKey().getName(),
                jobDetail.getKey().getGroup(),
                trigger.getJobKey().getName(),
                trigger.getJobKey().getGroup(),
                jobDetail.getJobClass().getName(),
                trigger.getCronExpression());
        // 启动
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    /**
     * 暂停任务
     *
     * @param obj 任务对象
     * @throws SchedulerException 定时任务异常
     * @method pause
     * @author krasus1966
     * @date 2022/5/2 19:35
     * @description 暂停任务
     */
    public void pause(BaseJob obj) throws SchedulerException {
        pause(obj.getJobKey());
    }

    /**
     * 暂停任务
     *
     * @param jobKey key,value对象
     * @throws SchedulerException 定时任务异常
     * @method pause
     * @author krasus1966
     * @date 2022/5/2 19:35
     * @description 暂停任务
     */
    public void pause(JobKey jobKey) throws SchedulerException {
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复任务
     *
     * @param obj 任务对象
     * @throws SchedulerException 定时任务异常
     * @method resume
     * @author krasus1966
     * @date 2022/5/2 19:36
     * @description 恢复任务
     */
    public void resume(BaseJob obj) throws SchedulerException {
        resume(obj.getJobKey());
    }

    /**
     * 恢复任务
     *
     * @param jobKey key,value对象
     * @return void
     * @throws SchedulerException 定时任务异常
     * @method resume
     * @author krasus1966
     * @date 2022/5/2 19:36
     * @description 恢复任务
     */
    public void resume(JobKey jobKey) throws SchedulerException {
        scheduler.resumeJob(jobKey);
    }

    /**
     * 触发一个中断, 对于的Job类必须实现InterruptableJob
     *
     * @param jobKey key,value对象
     * @return void
     * @throws SchedulerException 定时任务异常
     * @method interrupt
     * @author krasus1966
     * @date 2022/5/2 19:35
     * @description 触发一个中断, 对于的Job类必须实现InterruptableJob
     */
    public void interrupt(JobKey jobKey) throws SchedulerException {
        scheduler.interrupt(jobKey);
    }

    /**
     * 触发一个中断, 对于的Job类必须实现InterruptableJob
     *
     * @param obj 任务对象
     * @throws SchedulerException 定时任务异常
     * @method interrupt
     * @author krasus1966
     * @date 2022/5/2 19:35
     * @description 触发一个中断, 对于的Job类必须实现InterruptableJob
     */
    public void interrupt(BaseJob obj) throws SchedulerException {
        interrupt(obj.getJobKey());
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
    public void delete(BaseJob obj) throws SchedulerException {
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
    public boolean delete(JobKey jobKey) throws SchedulerException {
        return scheduler.deleteJob(jobKey);
    }

    /**
     * 是否存在特定的任务
     *
     * @param obj 任务对象
     * @return boolean
     * @throws SchedulerException 定时任务异常
     * @method exist
     * @author krasus1966
     * @date 2022/5/2 19:33
     * @description 是否存在特定的任务
     */
    public boolean exist(BaseJob obj) throws SchedulerException {
        return exist(obj.getJobKey());
    }

    /**
     * 是否存在特定的任务
     *
     * @param jobKey key,value
     * @return boolean
     * @throws SchedulerException 定时任务异常
     * @method exist
     * @author krasus1966
     * @date 2022/5/2 19:32
     * @description 是否存在特定的任务
     */
    public boolean exist(JobKey jobKey) throws SchedulerException {
        return scheduler.checkExists(jobKey);
    }

    /**
     * 清除所有的任务
     *
     * @throws SchedulerException 定时任务异常
     * @method clear
     * @author krasus1966
     * @date 2022/5/2 19:32
     * @description 清除所有的任务
     */
    public void clear() throws SchedulerException {
        scheduler.clear();
    }

    /**
     * 获取一个Job的状态, 当前仅支持StdScheduler
     *
     * @param obj 任务对象
     * @return org.quartz.Trigger.TriggerState
     * @throws SchedulerException 定时任务异常
     * @method getState
     * @author krasus1966
     * @date 2022/5/2 19:32
     * @description 获取一个Job的状态, 当前仅支持StdScheduler
     */
    public Trigger.TriggerState getState(BaseJob obj) throws SchedulerException {
        return scheduler.getTriggerState(obj.getTrigger().getKey());
    }


    /**
     * 获取任务
     *
     * @param name  job名称
     * @param group job分组
     * @return top.krasus1966.quartz.entity.BaseJob
     * @throws SchedulerException 定时任务异常
     * @method getJob
     * @author krasus1966
     * @date 2022/5/2 19:31
     * @description 获取任务
     */
    public BaseJob getJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        if (!scheduler.checkExists(jobKey)) {
            return null;
        }
        JobDetail jd = scheduler.getJobDetail(jobKey);
        Trigger trigger = scheduler.getTrigger(new TriggerKey(name, group));
        return new BaseJob(jobKey, trigger, jd, trigger.getStartTime(), trigger.getEndTime());
    }
}
