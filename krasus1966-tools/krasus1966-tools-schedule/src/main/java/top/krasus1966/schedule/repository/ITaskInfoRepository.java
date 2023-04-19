package top.krasus1966.schedule.repository;


import top.krasus1966.schedule.entity.TaskInfo;

import java.util.List;

/**
 * 由开发者手动实现，并注入，否则使用默认的JPA实现
 *
 * @author Krasus1966
 * @date 2022/5/2 16:55
 **/
public interface ITaskInfoRepository {

    /**
     * 通过id查询任务
     *
     * @param id 任务id
     * @return top.krasus1966.quartz.entity.TaskInfo
     * @method findById
     * @author krasus1966
     * @date 2022/5/2 18:20
     * @description 通过id查询任务
     */
    TaskInfo findById(String id);


    /**
     * 通过id更新
     *
     * @param taskInfo 任务对象
     * @return boolean
     * @method updateById
     * @author krasus1966
     * @date 2022/5/2 18:20
     * @description 通过id更新
     */
    boolean updateById(TaskInfo taskInfo);

    /**
     * ID对应任务设置为启动(失败的和结束的是否设置为启动由开发者决定)
     *
     * @param id 任务id
     * @return int
     * @method setTaskRunning
     * @author krasus1966
     * @date 2022/5/2 18:21
     * @description ID对应任务设置为启动
     */
    boolean setTaskRunning(String id);

    /**
     * 查询所有任务
     *
     * @return java.util.List<top.krasus1966.quartz.entity.TaskInfo>
     * @method findAll
     * @author krasus1966
     * @date 2022/5/2 18:49
     * @description 查询所有任务
     */
    List<TaskInfo> findAll();
}
