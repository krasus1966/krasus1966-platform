package top.krasus1966.core.db.mybatis_plus.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.core.db.service.IBaseService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2023/6/19 00:03
 **/
public abstract class AbstractMybatisBaseService<Mapper extends BaseMapper<Persistent>, Persistent extends AbstractPersistent> extends ServiceImpl<Mapper, Persistent> implements IBaseService<Persistent> {

    @Override
    public Class<Persistent> entityClass() {
        return getEntityClass();
    }

    @Override
    public Boolean insert(Persistent persistent) {
        beforeSave(persistent);
        boolean isSave = super.save(persistent);
        afterSave(persistent);
        return isSave;
    }

    @Override
    public Boolean update(Persistent persistent) {
        beforeUpdate(persistent);
        boolean isUpdate = super.updateById(persistent);
        afterUpdate(persistent);
        return isUpdate;
    }

    @Override
    public Boolean delete(String ids) {
        beforeDelete(ids);
        boolean isDelete = super.removeByIds(Arrays.asList(ids.split(Constants.Entity.SPLIT)));
        afterDelete(ids);
        return isDelete;
    }

    @Override
    public Persistent getById(String id) {
        beforeGetById(id);
        Persistent persistent = super.getById(id);
        afterGetById(persistent);
        return persistent;
    }

    @Override
    public List<Persistent> query(Persistent persistent) {
        return this.query(persistent, null);
    }

    @Override
    public List<Persistent> query(Persistent persistent, List<OrderItem> orderItems) {
        this.formatOrderItem(orderItems);
        beforeQuery(persistent);
        List<Persistent> persistentList = this.chainQuery(persistent).list();
        afterQuery(persistentList);
        return persistentList;
    }

    @Override
    public Page<Persistent> queryPage(Persistent persistent, PageDTO<Persistent> page) {
        this.formatOrderItem(page.orders());
        beforeQueryPage(persistent, page);
        PageDTO<Persistent> pageDTO = super.lambdaQuery(persistent).page(page);
        afterQueryPage(pageDTO);
        return pageDTO;
    }

    @Override
    public List<Map<String, Object>> options(Persistent obj, String key, String label, String keyName, String labelName) {
        key = StrUtil.toSymbolCase(key, '_');
        label = StrUtil.toSymbolCase(label, '_');
        QueryWrapper<Persistent> wrapper = new QueryWrapper<Persistent>(obj);
        wrapper.select(key + " AS " + keyName, label + " AS " + labelName).groupBy(key);
        return getBaseMapper().selectMaps(wrapper);
    }

    public void formatOrderItem(List<OrderItem> orders) {
        if (null != orders && !orders.isEmpty()) {
            orders.forEach(orderItem -> orderItem.setColumn(StrUtil.toSymbolCase(orderItem.getColumn(), '_')));
        }
    }

    protected QueryChainWrapper<Persistent> chainQuery(Persistent persistent) {
        return chainQuery(persistent, null);
    }

    protected QueryChainWrapper<Persistent> chainQuery(Persistent persistent, List<OrderItem> orders) {
        QueryChainWrapper<Persistent> chainWrapper = query();
        chainWrapper.setEntity(persistent);
        if (null != orders && !orders.isEmpty()) {
            for (OrderItem order : orders) {
                if (order.isAsc()) {
                    chainWrapper.orderBy(SqlInjectionUtils.check(order.getColumn()), order.isAsc(), StrUtil.toSymbolCase(order.getColumn(), '_'));
                }
            }
        }
        return chainWrapper;
    }
}
