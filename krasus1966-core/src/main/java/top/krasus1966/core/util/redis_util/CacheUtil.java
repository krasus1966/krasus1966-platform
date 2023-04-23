package top.krasus1966.core.util.redis_util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.krasus1966.core.util.spring.SpringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Krasus1966
 * @date 2022/10/30 16:11
 **/
public class CacheUtil {
    private static final StringRedisTemplate redisTemplate =
            SpringUtil.getBean(StringRedisTemplate.class);

    private static final ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

    private static final String CACHE_SIMPLE_LOCK = "CACHE_SIMPLE_LOCK:";

    private CacheUtil() {
    }

    /**
     * 判断key是否存在
     *
     * @param key key
     * @return boolean
     */
    public static boolean keyIsExist(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key
     * @return
     */
    public static Long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 实现命令：expire 设置过期时间，单位秒
     *
     * @param key
     * @return
     */
    public static void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：increment key，增加key一次
     *
     * @param key
     * @return
     */
    public static Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 实现命令：decrement key，减少key一次
     *
     * @param key
     * @return
     */
    public static Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    public static void del(String key) {
        redisTemplate.delete(key);
    }

    // String（字符串）

    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     *
     * @param key
     * @param value
     */
    public static void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     * @param timeout （以秒为单位）
     */
    public static void set(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     * @param timeout
     */
    public static void set(String key, String value, Long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     */
    public static void setObject(String key, Object value) throws JsonProcessingException {
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value));
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     * @param timeout （以秒为单位）
     */
    public static void setObject(String key, Object value, long timeout) throws JsonProcessingException {
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), timeout,
                TimeUnit.SECONDS);
    }

    /**
     * 执行set指令
     *
     * @param key   key
     * @param value 缓存数据
     * @param time  缓存时间
     * @param unit  时间单位
     * @throws JsonProcessingException 转换JSON异常
     * @author krasus1966
     * @date 2022/10/27 19:42
     * @description 执行set指令
     */
    public static void setObject(String key, Object value, Long time, TimeUnit unit) throws JsonProcessingException {
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), time, unit);
    }

    /**
     * 如果key不存在，则设置，如果存在，则报错
     *
     * @param key
     * @param value
     */
    public static void setnx60s(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value, 60, TimeUnit.SECONDS);
    }

    /**
     * 如果key不存在，则设置，如果存在，则报错
     *
     * @param key
     * @param value
     */
    public static void setnx(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 如果key不存在，则设置，如果存在，则报错
     *
     * @param key
     * @param value
     */
    public static void setnx(String key, String value, int second) {
        redisTemplate.opsForValue().setIfAbsent(key, value, second, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     *
     * @param key
     * @return value
     */
    public static String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     *
     * @param key
     * @return value
     */
    public static <T> T getObject(String key, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(redisTemplate.opsForValue().get(key), type);
    }

    // Hash（哈希表）

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     * 直接存储整个对象，对象按 property:value 方式生成map再存储
     *
     * @param key
     * @param value
     */
    public static void hset(String key, Object value, CopyOptions options) {
        Map<String, Object> map = BeanUtil.beanToMap(value, new HashMap<>(), options);
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 实现命令：HSET key value，将哈希表 key中的域 直接存储整个对象
     *
     * @param key
     * @param value
     */
    public static void hset(String key, Object value, Long time, CopyOptions options) {
        Map<String, Object> map = BeanUtil.beanToMap(value, new HashMap<>(), options);
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    public static void hset(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    public static void hset(String key, String field, String value, Long time) {
        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    public static void hset(String key, String field, Object value) throws JsonProcessingException {
        redisTemplate.opsForHash().put(key, field, objectMapper.writeValueAsString(value));
    }

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     * 直接存储整个对象，对象按 property:value 方式生成map再存储
     *
     * @param key
     * @param value
     */
    public static void hset(String key, Map<String, String> value, Long time) {
        redisTemplate.opsForHash().putAll(key, value);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     * 直接存储整个对象，对象按 property:value 方式生成map再存储
     *
     * @param key
     * @param value
     */
    public static void hset(String key, Map<String, String> value) {
        redisTemplate.opsForHash().putAll(key, value);
    }

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    public static void hset(String key, String field, Object value, Long time) throws JsonProcessingException {
        redisTemplate.opsForHash().put(key, field, objectMapper.writeValueAsString(value));
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public static Map<String, String> hget(String key) {
        Map<String, String> map = redisTemplate.<String, String>opsForHash().entries(key);
        if (map.isEmpty()) {
            // 4. 不存在，拦截
            return null;
        }
        return map;
    }

    public static <T> T hget(String key, T value) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        if (map.isEmpty()) {
            // 4. 不存在，拦截
            return null;
        }
        return BeanUtil.fillBeanWithMap(map, value, false);
    }

    public static String hget(String key, String field) {
        Object value = redisTemplate.opsForHash().get(key, field);
        if (null == value) {
            return null;
        }
        return (String) value;
    }

    public static <T> T hget(String key, String field, Class<T> type) throws JsonProcessingException {
        Object value = redisTemplate.opsForHash().get(key, field);
        if (null == value) {
            return null;
        }
        return objectMapper.readValue((String) value, type);
    }

    public static void hdel(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    // List（列表）

    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public static Long lpush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public static <T> Long lpush(String key, T value) throws JsonProcessingException {
        return redisTemplate.opsForList().leftPush(key, objectMapper.writeValueAsString(value));
    }

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     *
     * @param key
     * @return 列表key的头元素。
     */
    public static String lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     *
     * @param key
     * @return 列表key的头元素。
     */
    public static <T> T lpop(String key, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(redisTemplate.opsForList().leftPop(key), type);
    }

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public static Long rpush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public static <T> Long rpush(String key, T value) throws JsonProcessingException {
        return redisTemplate.opsForList().rightPush(key, objectMapper.writeValueAsString(value));
    }

    /**
     * 实现命令：RPOP key，移除并返回列表 key的尾元素。
     *
     * @param key
     * @return 列表key的尾元素。
     */
    public static String rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 实现命令：RPOP key，移除并返回列表 key的尾元素。
     *
     * @param key
     * @return 列表key的尾元素。
     */
    public static <T> T rpop(String key, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(redisTemplate.opsForList().rightPop(key), type);
    }

    /**
     * 简单方式加锁
     *
     * @param key        key
     * @param timeoutSec 加锁秒数
     * @return boolean
     * @method trySimpleLock
     * @author krasus1966
     * @date 2022/11/17 21:27
     * @description 简单方式加锁
     */
    public static boolean trySimpleLock(String key, long timeoutSec) {
        String threadId = key + "-" + Thread.currentThread().getId();
        Boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(CACHE_SIMPLE_LOCK + key,
                threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(isSuccess);
    }

    /**
     * 简单方式解锁
     *
     * @param key key
     * @return void
     * @method unlock
     * @author krasus1966
     * @date 2022/11/17 21:28
     * @description 简单方式解锁
     */
    public void unlock(String key) {
        String threadId = key + "-" + Thread.currentThread().getId();
        String value = redisTemplate.opsForValue().get(threadId);
        // 存储的value（即UUID-线程id相同）才释放锁
        if (threadId.equals(value)) {
            redisTemplate.delete(CACHE_SIMPLE_LOCK + key);
        }
    }
}
