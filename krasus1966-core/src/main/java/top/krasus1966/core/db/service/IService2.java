package top.krasus1966.core.db.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础Service，对IService扩展
 *
 * @author Krasus1966
 * @date 2022/4/15 23:15
 **/
public interface IService2<T> extends IService<T> {

    /**
     * 检查新增数据合法性
     *
     * @param obj 需要检查的对象
     * @method checkValidity
     * @author krasus1966
     * @date 2022/4/18 15:16
     * @description 检查数据合法性
     */
    default void checkInsertValidity(T obj) {
    }

    /**
     * 检查修改数据合法性
     *
     * @param obj 需要检查的对象
     * @return void
     * @method checkUpdateValidity
     * @author krasus1966
     * @date 2022/10/30 22:53
     * @description 检查修改数据合法性
     */
    // q: 为什么这里用default修饰
    default void checkUpdateValidity(T obj) {
    }

    /**
     * 检查删除数据合法性
     *
     * @param ids 数据ids
     * @return void
     * @method checkDeleteValidity
     * @author krasus1966
     * @date 2022/11/21 13:27
     * @description
     */
    default void checkDeleteValidity(String ids) {

    }
}
