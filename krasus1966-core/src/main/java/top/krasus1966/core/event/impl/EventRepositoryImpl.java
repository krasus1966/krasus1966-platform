package top.krasus1966.core.event.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.krasus1966.core.event.BaseEvent;
import top.krasus1966.core.event.EventPO;
import top.krasus1966.core.event.IEventRepository;
import top.krasus1966.core.service.IService2;

/**
 * @author Krasus1966
 * @date 2022/10/31 11:54
 **/
@Service
public class EventRepositoryImpl extends ServiceImpl<IEventMapper, EventPO> implements IEventRepository, IService2<EventPO> {
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
