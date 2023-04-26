package top.krasus1966.core.event;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;
import top.krasus1966.core.web.entity.R;

/**
 * @author baiyan
 */
@Aspect
@Component
@Slf4j
@MapperScan("top.krasus1966.base_project.common.core.event.impl")
public class SaveEventHandler {

    private IEventRepository repository;

    public SaveEventHandler(IEventRepository repository) {
        this.repository = repository;
    }

    @Pointcut("@annotation(NeedSaveEvent)")
    public void pointcut() {
    }

    @AfterReturning(value = "pointcut()", returning = "r")
    public void afterReturning(JoinPoint joinPoint, R r) {
        BaseEvent baseEvent = (BaseEvent) joinPoint.getArgs()[0];
        if (r.isSuccessful()) {
            // 更新事件状态为成功
            log.info("更新事件{}状态为成功", baseEvent.getId());
            baseEvent.handleSuccess();
        } else {
            // 更新状态失败
            log.info("更新事件{}状态为失败", baseEvent.getId());
            baseEvent.handleFailed();
        }
        repository.update(baseEvent);
    }
}
