package top.krasus1966.schedule.repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.krasus1966.schedule.entity.TaskInfo;

import java.util.List;

/**
 * TaskInfo表仓储服务
 *
 * @author Krasus1966
 * @date 2022/5/2 20:05
 **/
@Repository
public interface TaskInfoRepo extends CrudRepository<TaskInfo, String> {
    @Override
    List<TaskInfo> findAll();
}
