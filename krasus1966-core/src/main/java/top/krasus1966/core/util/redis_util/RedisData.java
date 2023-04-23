package top.krasus1966.core.util.redis_util;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 逻辑缓存统一封装对象
 *
 * @author krasus1966
 * @date 2022/10/27 21:04
 * @description 逻辑缓存统一封装对象
 */
@Data
public class RedisData<T> implements Serializable {
    private LocalDateTime expireTime;
    private T data;
}
