package top.krasus1966.quartz.job;

import cn.hutool.core.text.CharSequenceUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.krasus1966.quartz.entity.TaskInfo;
import top.krasus1966.quartz.repository.ITaskInfoRepository;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Krasus1966
 * @date 2022/5/2 16:07
 **/
@Component
@DisallowConcurrentExecution
public abstract class TaskJob implements Job {

    public static final Logger log = LoggerFactory.getLogger(TaskJob.class);

    @Resource
    private ITaskInfoRepository taskInfoRepository;


    /**
     * 子任务执行
     *
     * @param jobDataMap 子任务参数
     * @return java.lang.String
     * @method work
     * @author krasus1966
     * @date 2022/5/2 18:22
     * @description 子任务执行
     */
    public abstract String work(JobDataMap jobDataMap);


    /**
     * 定时任务逻辑
     *
     * @param context
     * @method execute
     * @author krasus1966
     * @date 2022/5/2 18:26
     * @description 定时任务核心逻辑
     */
    @Override
    public void execute(JobExecutionContext context) {
        boolean isRunning = running(context);
        String taskInfo = "";
        boolean isSucc = true;
        if (isRunning) {
            long beginTime = System.currentTimeMillis();
            try {
                taskInfo = work(context.getJobDetail().getJobDataMap());
            } catch (Exception e) {
                log.error("定时任务执行失败", e);
                isSucc = false;
            }
            long endTime = System.currentTimeMillis();
            workAfter(context, endTime - beginTime, isSucc, taskInfo);
        }
    }

    /**
     * 启动任务
     *
     * @param context job上下文
     * @return int
     * @method running
     * @author krasus1966
     * @date 2022/5/2 18:23
     * @description 启动任务
     */
    private boolean running(JobExecutionContext context) {
        try {
            // 获取任务id
            String taskId = context.getJobDetail().getKey().getName();
            // 设置任务为运行状态
            return taskInfoRepository.setTaskRunning(taskId);
        } catch (Exception e) {
            log.error("running", e);
        }
        return false;
    }

    /**
     * 任务执行完毕后执行
     *
     * @param context 上下文
     * @param cost    耗费时间
     * @param isSucc  是否成功
     * @param runInfo 返回值
     * @return void
     * @method workAfter
     * @author krasus1966
     * @date 2022/5/2 18:23
     * @description 任务执行完毕后执行
     */
    private void workAfter(JobExecutionContext context, long cost, boolean isSucc, String runInfo) {
        String taskId = CharSequenceUtil.trimToEmpty(context.getJobDetail().getKey().getName());
        TaskInfo taskInfo = taskInfoRepository.findById(taskId);
        if (taskInfo != null) {
            try {
                int taskStatus = 0;
                if (context.getNextFireTime() == null) {
                    //不会再执行
                    taskStatus = TaskInfo.TaskStatus.TASK_FINISH;
                } else {
                    //等待执行
                    taskStatus = TaskInfo.TaskStatus.TASK_SLEEP;
                }
                Date date = new Date();
                taskInfo.setLastTime(date)
                        .setLastInfo(CharSequenceUtil.trimToEmpty(runInfo))
                        .setStatus(taskStatus)
                        .setNextTime(context.getNextFireTime())
                        .setRunCount((taskInfo.getRunCount() == null ? 1 : (taskInfo.getRunCount() + 1)))
                        .setLastDuration(cost);
                if (isSucc) {
                    taskInfo.setLastSuccessTime(date);
                }

                boolean result = taskInfoRepository.updateById(taskInfo);
                if (!result) {
                    log.debug("Task is not exist  (taskId = " + taskId + ")");
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }
}
