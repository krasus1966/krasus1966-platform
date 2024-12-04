package top.krasus1966.common.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.krasus1966.common.core.entity.AbstractEntity;
import top.krasus1966.common.core.entity.PageDTO;
import top.krasus1966.common.db.entity.AbstractPersistent;

import java.util.List;
import java.util.Map;

public interface ICommonService<Entity extends AbstractEntity, Persistent extends AbstractPersistent> {

    String check(Entity entity);

    default void beforeSave(Entity entity, Persistent persistent, Map<String, Object> context) throws Exception {
    }

    default void afterSave(Entity entity, Persistent persistent, boolean success, Map<String, Object> context) throws Exception {
    }

    default void beforeUpdate(Entity entity, Persistent persistent, Map<String, Object> context) throws Exception {
    }

    default void afterUpdate(Entity entity, Persistent persistent, boolean success, Map<String, Object> context) throws Exception {
    }

    default void beforeDeleteByIds(String ids, Map<String, Object> context) throws Exception {
    }

    default void afterDeleteByIds(String ids, boolean success, Map<String, Object> context) throws Exception {
    }

    default void beforeView(String id, Map<String, Object> context) throws Exception {
    }

    default void afterView(Entity entity, Map<String, Object> context) throws Exception {
    }

    default void beforeQuery(QueryWrapper<Persistent> queryWrapper, Map<String, Object> context) throws Exception {
    }

    default void afterQuery(List<Entity> entityList, Map<String, Object> context) throws Exception {
    }

    default void beforeQueryPage(QueryWrapper<Persistent> queryWrapper, Page<Persistent> page, Map<String, Object> context) throws Exception {
    }

    default void afterQueryPage(PageDTO<Entity> dPr, Map<String, Object> context) throws Exception {
    }
}
