package top.krasus1966.schedule.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import top.krasus1966.schedule.config.JacksonEnum;
import top.krasus1966.schedule.entity.BaseJob;
import top.krasus1966.schedule.entity.JobKey;
import top.krasus1966.schedule.job.TaskJob;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Krasus1966
 * @date 2023/4/19 22:03
 **/
@Service
public class TaskManageService implements SchedulingConfigurer {
    public static final Logger log = LoggerFactory.getLogger(TaskManageService.class);
    public final ConcurrentHashMap<JobKey, ScheduledFuture<?>> runningTask =
            new ConcurrentHashMap<>();
    public final ConcurrentHashMap<JobKey, BaseJob> runningTaskJob = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private ScheduledTaskRegistrar scheduledTaskRegistrar;
    private ApplicationContext applicationContext;

    public TaskManageService(ApplicationContext applicationContext) {
        this.objectMapper = JacksonEnum.INSTANCE.getObjectMapper();
        this.applicationContext = applicationContext;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        this.scheduledTaskRegistrar = taskRegistrar;
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
            Class<? extends TaskJob> klass =
                    (Class<? extends TaskJob>) Class.forName(obj.getClassName());
            Map<String, String> tmp = null;
            if (!Strings.isBlank(obj.getDataMap())) {
                JavaType javaType =
                        objectMapper.getTypeFactory().constructParametricType(String.class,
                                String.class);
                tmp = objectMapper.readValue(obj.getDataMap(), javaType);
            }
            Map<String, String> data = tmp == null ? new HashMap<>() : tmp;
            obj.setRealDataMap(data);
            // 构建一个触发器，规定触发的规则
            CronTrigger trigger = new CronTrigger(obj.getCron());
            SimpleTriggerContext triggerContext = new SimpleTriggerContext();
            if (null == obj.getBeginTime()) {
                // 未设置开始时间

            } else {
                // 设置了开始时间
            }
            if (null != obj.getEndTime()) {
                // 设置了结束时间
            }
            if (exist(obj.getJobKey())) {
                // 存在则更新任务
                delete(obj);
                addJob(obj, klass, trigger);
            } else {
                // 不存在则新增任务
                addJob(obj, klass, trigger);
            }
        } catch (Exception e) {
            log.error("新建任务失败", e);
        }
    }

    private void addJob(BaseJob obj, Class<? extends TaskJob> klass, CronTrigger trigger) {
        log.error("添加任务:{},{},{},{}",
                obj.getJobName(),
                obj.getJobGroup(),
                obj.getClassName(),
                trigger.getExpression());
        TaskScheduler scheduler = scheduledTaskRegistrar.getScheduler();
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            TaskJob bean = applicationContext.getBean(klass);
            bean.registerJob(obj);
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
    public void delete(BaseJob obj) {
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
    public boolean exist(BaseJob obj) {
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
    public BaseJob getJob(String name, String group) {
        JobKey jobKey = new JobKey(name, group);
        if (!exist(jobKey)) {
            return null;
        }
        return runningTaskJob.get(jobKey);
    }
}
