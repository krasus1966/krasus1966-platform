package top.krasus1966.core.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Krasus1966
 * @date 2022/4/11 21:09
 **/
@Data
@NoArgsConstructor
public class BaseEvent<T> {

    private String id;

    private T data;

    private LocalDateTime timestamp;

    private EventStatusEnum status;

    private BaseEventEnum type;

    public BaseEvent(String id, T data, EventStatusEnum status, BaseEventEnum type) {
        this.id = id;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.type = type;
    }

    /**
     * 修改事件状态为成功
     */
    public void handleSuccess() {
        this.status = EventStatusEnum.SUCCESS;
    }

    /**
     * 修改事件状态为失败
     */
    public void handleFailed() {
        this.status = EventStatusEnum.FAILED;
    }

}
