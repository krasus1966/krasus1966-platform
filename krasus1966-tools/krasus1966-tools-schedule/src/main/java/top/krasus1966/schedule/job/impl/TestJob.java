package top.krasus1966.schedule.job.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.krasus1966.schedule.job.TaskJob;

import java.util.Map;

/**
 * @author Krasus1966
 * @date 2023/4/19 23:53
 **/
@Slf4j
@Component
public class TestJob extends TaskJob {

    @Override
    public String work(Map<String, String> jobDataMap) {
        log.error("执行TestJob");
        return null;
    }
}
