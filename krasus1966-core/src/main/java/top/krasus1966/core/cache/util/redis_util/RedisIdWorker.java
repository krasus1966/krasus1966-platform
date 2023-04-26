package top.krasus1966.core.cache.util.redis_util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 使用redis自增生成id
 * 时间戳左移N位 + 自增数补到32位 = id
 *
 * @author Krasus1966
 * @date 2022/10/28 09:56
 **/
@Component
public class RedisIdWorker {

    /**
     * 开始时间戳 2022/01/01 00:00:00
     */
    private static final long BEGIN_TIMESTAMP = 1640995200L;
    /**
     * 序列号的位数
     */
    private static final int COUNT_BITS = 32;

    private final StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 生成id
     *
     * @param keyPrefix key前缀
     * @return long
     * @method nextId
     * @author krasus1966
     * @date 2022/10/31 17:22
     * @description 生成id
     */
    public long nextId(String keyPrefix) {
        // 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSeconds = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSeconds - BEGIN_TIMESTAMP;
        // 生成序列号
        // 获取当前日期，精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 自增长
        long count =
                stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date).longValue();
        // 返回
        return timestamp << COUNT_BITS | count;
    }
}
