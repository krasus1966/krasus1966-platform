package top.krasus1966.common.core.func;


import top.krasus1966.common.core.entity.AbstractDTO;
import top.krasus1966.common.core.entity.AbstractEntity;
import top.krasus1966.common.core.entity.PageDTO;

import java.util.List;
import java.util.Map;

/**
 * Facade操作切入点
 *
 * @author krasus1966
 * @date 2024/10/15 11:20
 **/
public interface IPointFacade<Entity extends AbstractEntity, DTO extends AbstractDTO> {

    default void beforeInsert(Entity entity, Map<String, Object> context) throws Exception {
    }

    default void afterInsert(Entity entity, boolean isSuccess, Map<String, Object> context) throws Exception {
    }

    default void beforeUpdate(Entity obj, Map<String, Object> context) throws Exception {
    }

    default void afterUpdate(Entity obj, boolean isSuccess, Map<String, Object> context) throws Exception {
    }

    default void beforeDeletes(String ids, Map<String, Object> context) throws Exception {
    }

    default void afterDeletes(String ids, boolean isSuccess, Map<String, Object> context) throws Exception {
    }

    default void beforeQuery(Map<String, Object> context) throws Exception {
    }

    default void afterQuery(List<DTO> dtoList, Map<String, Object> context) throws Exception {
    }

    default void beforeQueryPage(Map<String, Object> context) throws Exception {
    }

    default void afterQueryPage(PageDTO<DTO> dtoPr, Map<String, Object> context) throws Exception {
    }

    default void beforeView(String id, Map<String, Object> context) throws Exception {
    }

    default void afterView(DTO dto, Map<String, Object> context) throws Exception {
    }
}
