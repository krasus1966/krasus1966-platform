package top.krasus1966.core.event;

/**
 * @author Krasus1966
 * @date 2022/4/17 17:14
 **/
public interface IEventRepository {

    BaseEvent findById(String id);

    void save(BaseEvent obj);

    void update(BaseEvent obj);
}
