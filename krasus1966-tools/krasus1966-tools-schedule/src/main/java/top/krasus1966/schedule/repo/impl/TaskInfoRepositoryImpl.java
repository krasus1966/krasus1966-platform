package top.krasus1966.schedule.repo.impl;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import top.krasus1966.schedule.entity.TaskInfo;
import top.krasus1966.schedule.repo.TaskInfoRepo;
import top.krasus1966.schedule.repository.ITaskInfoRepository;

import java.util.List;

/**
 * 使用JPA默认实现的ITaskInfoRepository
 *
 * @author Krasus1966
 * @date 2022/5/2 20:07
 **/
@Service
// 扫描自身需要使用的内容
@EntityScan("top.krasus1966.schedule.*")
@EnableJpaRepositories("top.krasus1966.schedule.*")
public class TaskInfoRepositoryImpl implements ITaskInfoRepository {

    private final TaskInfoRepo repo;

    public TaskInfoRepositoryImpl(TaskInfoRepo repo) {
        this.repo = repo;
    }

    @Override
    public TaskInfo findById(String id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public boolean updateById(TaskInfo taskInfo) {
        if (repo.existsById(taskInfo.getId())) {
            repo.save(taskInfo);
            return true;
        }
        return false;
    }

    @Override
    public boolean setTaskRunning(String id) {
        TaskInfo taskInfo = repo.findById(id).orElse(null);
        if (null != taskInfo && !TaskInfo.TaskStatus.TASK_END.equals(taskInfo.getStatus()) && !TaskInfo.TaskStatus.TASK_FINISH.equals(taskInfo.getStatus())) {
            taskInfo.setStatus(TaskInfo.TaskStatus.TASK_RUNNING);
            repo.save(taskInfo);
            return true;
        }
        return false;
    }

    @Override
    public List<TaskInfo> findAll() {
        return repo.findAll();
    }
}
