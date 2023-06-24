package top.krasus1966.core.event.impl;

import org.springframework.stereotype.Service;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.core.event.IEventRepository;
import top.krasus1966.core.event.entity.BaseEvent;
import top.krasus1966.core.event.entity.db.EventPO;

/**
 * @author Krasus1966
 * @date 2022/10/31 11:54
 **/
@Service
public class EventRepositoryImpl extends AbstractMybatisBaseService<IEventMapper, EventPO> implements IEventRepository {
    @Override
    public BaseEvent findById(String id) {
        return baseMapper.selectById(id).toBaseEvent();
    }

    @Override
    public void save(BaseEvent obj) {
        save(new EventPO(obj));
    }

    @Override
    public void update(BaseEvent obj) {
        updateById(new EventPO(obj));
    }
}
