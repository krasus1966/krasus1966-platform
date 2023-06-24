package top.krasus1966.core.event.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import top.krasus1966.core.event.IEventRepository;
import top.krasus1966.core.event.entity.BaseEvent;

/**
 * @author Krasus1966
 * @date 2022/4/17 17:19
 **/
@Component
@Slf4j
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final IEventRepository eventRepository;

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher,
                          IEventRepository eventRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.eventRepository = eventRepository;
    }

    public <EVENT extends BaseEvent> void publish(EVENT event) {
        applicationEventPublisher.publishEvent(event);
    }

    public <EVENT extends BaseEvent> void publishAndSave(EVENT event) {
        eventRepository.save(event);
        applicationEventPublisher.publishEvent(event);
    }
}
