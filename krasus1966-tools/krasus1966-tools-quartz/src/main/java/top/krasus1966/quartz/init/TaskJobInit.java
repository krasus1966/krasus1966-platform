package top.krasus1966.quartz.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.krasus1966.quartz.entity.BaseJob;
import top.krasus1966.quartz.service.TaskManageService;

/**
 * @author Krasus1966
 * @date 2022/5/2 20:53
 **/
@Component
public class TaskJobInit implements ApplicationRunner {

    private final TaskManageService service;

    public TaskJobInit(TaskManageService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 启动时默认启动扫描任务
        service.add(new BaseJob().setId("defalut")
                .setCron("0/5 * * * * ?")
                .setClassName("top.krasus1966.quartz.job.TaskScanJob")
                .setJobName("TaskScan")
                .setJobGroup("TaskScan"));
    }
}
