package top.krasus1966.schedule.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import top.krasus1966.schedule.entity.BaseJob;
import top.krasus1966.schedule.entity.JobKey;
import top.krasus1966.schedule.entity.TaskInfo;
import top.krasus1966.schedule.repository.ITaskInfoRepository;
import top.krasus1966.schedule.service.TaskManageService;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/4/19 22:22
 **/
@Component
public class TaskScanJob implements SchedulingConfigurer {

    public static final Logger log = LoggerFactory.getLogger(TaskScanJob.class);

    private final ITaskInfoRepository repository;

    private final TaskManageService manageService;

    public TaskScanJob(ITaskInfoRepository repository, TaskManageService manageService) {
        this.repository = repository;
        this.manageService = manageService;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(new CronTask(() -> {
            try {
                log.error("开始扫描任务，线程:{}", Thread.currentThread().getName());
                //获取全部任务
                List<TaskInfo> taskInfoList = repository.findAll();
                if (taskInfoList != null && !taskInfoList.isEmpty()) {
                    for (TaskInfo taskInfo : taskInfoList) {
                        checkTaskInfo(taskInfo);
                    }
                }
            } catch (Exception e) {
                log.error("加载任务列表失败", e);
            }
            log.error("扫描完成，线程:{}", Thread.currentThread().getName());
        },"0/5 * * * * ?"));
    }

    /**
     * 检查并处理任务
     *
     * @param taskInfo 任务对象
     * @throws SchedulerException 任务异常
     * @method checkTaskInfo
     * @author krasus1966
     * @date 2022/5/2 23:29
     * @description 检查并处理任务
     */
    private void checkTaskInfo(TaskInfo taskInfo) {
        //判断任务是否在当前任务管理器中
        boolean isExist = manageService.exist(new JobKey(taskInfo.getId(), taskInfo.getId()));
        // 如果存在
        if (isExist) {
            //从任务管理器中获取这个任务
            BaseJob baseJob = manageService.getJob(taskInfo.getId(), taskInfo.getId());
            //判断当前表中任务是否被删除了
            if (taskInfo.isDel()) {
                //如果表中删除了，则任务管理器中也要删除
                log.error("任务已被删除:{}", baseJob.getClassName());
                manageService.delete(baseJob);
            } else if (taskInfo.isStop()) {
                // 如果表中停止，则任务管理器中删除
                log.error("任务手动结束:{}", baseJob.getClassName());
                manageService.delete(baseJob);
            } else {
                //如果表中没有删除，判断任务管理器中的任务与数据库中任务是否关键字段是否改变
                //如果改变了，那么就重新将人无添加到任务管理器中
                if (baseJob.isChange(taskInfo)) {
                    //任务是否覆盖？？？有争议
                    manageService.add(baseJob.getJobByTaskInfo(taskInfo));
                }
            }
        } else { // 不存在 时 如果任务没有被删除，并且不是待执行
            if (!taskInfo.isDel()) {
                if (TaskInfo.TaskStatus.TASK_WAITING.equals(taskInfo.getStatus())
                        || TaskInfo.TaskStatus.TASK_RUNNING.equals(taskInfo.getStatus())
                        || TaskInfo.TaskStatus.TASK_SLEEP.equals(taskInfo.getStatus())) {
                    BaseJob baseJob = new BaseJob();
                    manageService.add(baseJob.getJobByTaskInfo(taskInfo));
                }
            }
        }
    }
}
