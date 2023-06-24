package top.krasus1966.core.db.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import top.krasus1966.core.db.IServiceHook;
import top.krasus1966.core.db.entity.AbstractPersistent;

import java.util.List;
import java.util.Map;

/**
 * 基础Service，对IService扩展
 *
 * @author Krasus1966
 * @date 2022/4/15 23:15
 **/
public interface IBaseService<Persistent extends AbstractPersistent> extends IServiceHook<Persistent> {

    Boolean insert(Persistent persistent);

    Boolean update(Persistent persistent);

    Boolean delete(String ids);

    Persistent getById(String id);

    List<Persistent> query(Persistent persistent);

    List<Persistent> query(Persistent persistent, List<OrderItem> orderItems);

    Page<Persistent> queryPage(Persistent persistent, PageDTO<Persistent> page);

    List<Map<String, Object>> options(Persistent obj,String key,String label,String keyName,String labelName);

    void formatOrderItem(List<OrderItem> orders);
}
