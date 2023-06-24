package top.krasus1966.core.db.hook;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import top.krasus1966.core.db.entity.AbstractPersistent;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/6/24 18:55
 **/
public interface IServiceHook<Persistent extends AbstractPersistent> {

    default void beforeGetById(String id) {

    }

    default void afterGetById(Persistent persistent) {

    }

    default void beforeSave(Persistent persistent) {
    }

    default void afterSave(Persistent persistent) {
    }

    default void beforeUpdate(Persistent persistent) {
    }

    default void afterUpdate(Persistent persistent) {
    }

    default void beforeDelete(String ids) {
    }

    default void afterDelete(String ids) {
    }

    default void beforeQuery(Persistent persistent) {
    }

    default void afterQuery(List<Persistent> persistent) {
    }

    default void beforeQueryPage(Persistent persistent, PageDTO<Persistent> page) {

    }

    default void afterQueryPage(PageDTO<Persistent> pageDTO) {

    }
}
