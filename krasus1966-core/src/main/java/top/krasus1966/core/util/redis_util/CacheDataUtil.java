package top.krasus1966.core.util.redis_util;

import cn.hutool.core.text.CharSequenceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.krasus1966.core.exception.JsonCastException;
import top.krasus1966.core.util.spring.SpringUtil;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 数据缓存工具
 *
 * @author Krasus1966
 * @date 2022/10/26 11:26
 **/
public class CacheDataUtil {

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
    private static final String LOCK_KEY_PREFIX = "lock:data:";
    private static final StringRedisTemplate stringRedisTemplate =
            SpringUtil.getBean(StringRedisTemplate.class);
    private static final ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

    private CacheDataUtil() {
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
    public static void set(String key, Object value, Long time, TimeUnit unit) throws JsonProcessingException {
        stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), time,
                unit);
    }

    /**
     * 执行get指令
     *
     * @param key key
     * @return java.lang.String
     * @method get
     * @author krasus1966
     * @date 2022/10/27 21:07
     * @description 执行get指令
     */
    public static String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 缓存-逻辑时间
     *
     * @param key   key
     * @param value 数据值
     * @param time  缓存时间
     * @param unit  时间单位
     * @throws JsonProcessingException JSON转换异常
     * @method setWithLogicalExpire
     * @author krasus1966
     * @date 2022/10/27 19:49
     * @description 缓存-逻辑时间
     */
    public static <R> void setWithLogicalExpire(String key, R value, Long time, TimeUnit unit) throws JsonProcessingException {
        RedisData<R> redisData = new RedisData<>();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(redisData));
    }

    /**
     * 缓存并获取单一对象
     * 空值会被存储和判断，以避免可能出现的缓存穿透
     *
     * @param prefixKey     key前缀
     * @param id            数据唯一标识
     * @param type          数据类型
     * @param time          数据存储时间
     * @param unit          数据时间单位
     * @param nullValueTime 空值存储时间
     * @param nullValueUnit 空值时间单位
     * @param dbFallback    数据库执行方法
     * @return R
     * @throws JsonProcessingException 转换JSON异常
     * @method getWithPassThrough
     * @author krasus1966
     * @date 2022/10/27 19:41
     * @description 缓存单一对象 空值会被存储和判断
     */
    public static <R, ID> R getWithPassThrough(String prefixKey, ID id, Class<R> type, Long time,
                                               TimeUnit unit, Long nullValueTime,
                                               TimeUnit nullValueUnit,
                                               Function<ID, R> dbFallback) throws JsonProcessingException {
        String redisKey = prefixKey + id;
        String cache = get(redisKey);
        if (CharSequenceUtil.isNotBlank(cache)) {
            return objectMapper.readValue(cache, type);
        }
        if (cache != null) {
            return null;
        }
        R r = dbFallback.apply(id);
        if (r == null) {
            // 数据不存在，储存空值
            set(redisKey, "", nullValueTime, nullValueUnit);
            return null;
        }
        // 数据存在，保存到缓存
        set(redisKey, r, time, unit);
        return r;
    }

    /**
     * 互斥锁方式获取数据
     * 空值会被存储和判断，以避免可能出现的缓存穿透
     * <p>重要：线程阻塞，存在死锁的可能</p>
     *
     * @param prefixKey     key前缀
     * @param id            数据唯一索引
     * @param type          数据类型
     * @param time          缓存时间
     * @param unit          时间单位
     * @param nullValueTime 空值缓存时间
     * @param nullValueUnit 空值缓存时间单位
     * @param lockTime      锁的时间
     * @param lockUnit      锁的时间单位
     * @param dbFallback    数据库执行方法
     * @return R
     * @throws JsonProcessingException JSON转换异常
     * @throws InterruptedException    线程中断异常
     * @method getWithMutex
     * @author krasus1966
     * @date 2022/10/27 21:42
     * @description 互斥锁方式获取数据
     */
    public static <R, ID> R getWithMutex(String prefixKey, ID id, Class<R> type, Long time,
                                         TimeUnit unit, Long nullValueTime,
                                         TimeUnit nullValueUnit, Long lockTime, TimeUnit lockUnit
            , Function<ID, R> dbFallback) throws JsonProcessingException, InterruptedException {
        String redisKey = prefixKey + id;
        String redisLockKey = LOCK_KEY_PREFIX + prefixKey + id;
        // 从缓存获取数据
        String cache = stringRedisTemplate.opsForValue().get(redisKey);
        // 判断数据是否存在
        R object = null;
        if (CharSequenceUtil.isNotBlank(cache)) {
            object = objectMapper.readValue(cache, type);
            return object;
        }
        if (cache != null) {
            return null;
        }
        // 实现缓存重建
        // 生成锁唯一ID，解锁时先获取并判断值是否相等，不相等则不可解锁
        String lockValue = UUID.randomUUID().toString();
        try {
            // 获取互斥锁
            boolean isLock = tryLock(redisLockKey, lockValue, lockTime, lockUnit);
            // 自旋判断是否抢锁成功
            while (!isLock) {
                // 失败，休眠并重试获取锁
                Thread.sleep(500);
                isLock = tryLock(redisLockKey, lockValue, lockTime, lockUnit);
            }
            // 成功抢锁，查询缓存中是否存在数据
            cache = stringRedisTemplate.opsForValue().get(redisKey);
            // 判断数据是否存在
            if (CharSequenceUtil.isNotBlank(cache)) {
                object = objectMapper.readValue(cache, type);
                return object;
            }
            if (cache != null) {
                return null;
            }
            // 成功抢锁，依旧没数据，查询数据库并更新缓存
            object = dbFallback.apply(id);
            if (null == object) {
                // 数据不存在，储存空值
                stringRedisTemplate.opsForValue().set(redisKey, "", nullValueTime, nullValueUnit);
                return null;
            }
            // 数据存在，保存到缓存
            stringRedisTemplate.opsForValue().set(redisKey,
                    objectMapper.writeValueAsString(object), time, unit);
            return object;
        } finally {
            // 获取锁的值
            String value = get(redisLockKey);
            if (CharSequenceUtil.isNotBlank(value) && lockValue.equals(value)) {
                // 当前锁的值和缓存锁的值相同，释放互斥锁
                unLock(redisLockKey);
            }
        }
    }

    /**
     * 逻辑缓存-缓存单一对象
     * 数据持久化，并存储过期时间，若系统时间 > 过期时间，认为缓存已过期，进行数据重建
     * 避免可能出现的缓存击穿
     * <p>需要手动初始化数据！</p>
     *
     * @param prefixKey  key前缀
     * @param id         数据唯一索引
     * @param type       数据类型
     * @param time       缓存时间
     * @param unit       时间单位
     * @param lockTime   锁的时间
     * @param lockUnit   锁的时间单位
     * @param dbFallback 数据库执行方法
     * @return R
     * @throws JsonProcessingException JSON转换异常
     * @method queryWithLogicalExpire
     * @author krasus1966
     * @date 2022/10/27 21:02
     * @description 逻辑缓存-缓存单一对象
     */
    public static <R, ID> R getWithLogicalExpire(String prefixKey, ID id, Class<R> type,
                                                 Long time, TimeUnit unit, Long lockTime,
                                                 TimeUnit lockUnit, Function<ID, R> dbFallback) throws JsonProcessingException {
        String redisKey = prefixKey + id;
        String redisLockKey = LOCK_KEY_PREFIX + redisKey;
        // 从缓存获取数据
        String cache = stringRedisTemplate.opsForValue().get(redisKey);
        // 判断数据是否存在
        if (CharSequenceUtil.isBlank(cache)) {
            // 数据不存在，执行缓存重建，存在问题
            rebuildLogicalExpireCache(redisKey, redisLockKey, id, time, unit, lockTime, lockUnit,
                    dbFallback);
            // 先返回空值
            return null;
        }
        // 命中，反序列化json
        RedisData<R> redisData = objectMapper.readValue(cache,
                objectMapper.getTypeFactory().constructParametricType(RedisData.class, type));
        R object = redisData.getData();
        LocalDateTime expireTime = redisData.getExpireTime();
        // 判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 未过期，返回缓存数据信息
            return object;
        }
        // 已过期，开始缓存重建
        rebuildLogicalExpireCache(redisKey, redisLockKey, id, time, unit, lockTime, lockUnit,
                dbFallback);
        // 返回旧数据
        return object;
    }

    /**
     * 执行缓存重建/初始化数据
     * 先抢锁，抢锁失败直接返回null
     * 抢锁成功，执行构建指令
     *
     * @param key          key
     * @param redisLockKey 锁的key
     * @param id           数据唯一索引
     * @param time         缓存时间
     * @param unit         时间单位
     * @param lockTime     锁缓存时间
     * @param lockUnit     锁时间单位
     * @param dbFallback   数据库执行方法
     * @method rebuildLogicalExpireCache
     * @author krasus1966
     * @date 2022/10/27 20:57
     * @description 执行缓存重建/初始化数据
     */
    public static <R, ID> void rebuildLogicalExpireCache(String key, String redisLockKey, ID id,
                                                         Long time, TimeUnit unit, Long lockTime,
                                                         TimeUnit lockUnit,
                                                         Function<ID, R> dbFallback) {
        if (tryLock(redisLockKey, lockTime, lockUnit)) {
            try {
                // 获取锁成功，且数据不存在，开启新线程更新数据，并返回旧数据
                CACHE_REBUILD_EXECUTOR.submit(() -> {
                    try {
                        // 查询数据库
                        R r = dbFallback.apply(id);
                        // 写入redis
                        setWithLogicalExpire(key, r, time, unit);
                    } catch (JsonProcessingException e) {
                        throw new JsonCastException(e);
                    }
                });
            } finally {
                unLock(redisLockKey);
            }
        }
    }

    /**
     * 加锁-默认单位秒-值为UUID
     *
     * @param lockKey    锁的key
     * @param expireTime 锁时间
     * @return boolean 加锁成功
     * @method tryLock
     * @author krasus1966
     * @date 2022/10/27 19:55
     * @description 加锁
     */
    public static boolean tryLock(String lockKey, Long expireTime) {
        return tryLock(lockKey, UUID.randomUUID().toString(), expireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 加锁-无默认时间单位-默认值UUID
     *
     * @param lockKey    锁的key
     * @param expireTime 缓存时间
     * @param unit       时间单位
     * @return boolean
     * @method tryLock
     * @author krasus1966
     * @date 2022/10/27 20:29
     * @description 加锁-无默认时间单位-默认值UUID
     */
    public static boolean tryLock(String lockKey, Long expireTime, TimeUnit unit) {
        return tryLock(lockKey, UUID.randomUUID().toString(), expireTime, unit);
    }

    /**
     * 加锁-默认单位秒-无默认值
     *
     * @param lockKey    锁的key
     * @param value      缓存值
     * @param expireTime 缓存时间
     * @return boolean
     * @method tryLock
     * @author krasus1966
     * @date 2022/10/27 20:27
     * @description 加锁-默认单位秒-无默认值
     */
    public static boolean tryLock(String lockKey, String value, Long expireTime) {
        return tryLock(lockKey, value, expireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 加锁-无默认时间单位和默认值
     *
     * @param lockKey    锁的key
     * @param value      缓存值
     * @param expireTime 缓存时间
     * @param unit       时间单位
     * @return boolean
     * @method tryLock
     * @author krasus1966
     * @date 2022/10/27 20:26
     * @description 加锁-无默认时间单位和默认值
     */
    public static boolean tryLock(String lockKey, String value, Long expireTime, TimeUnit unit) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(lockKey, value,
                expireTime, unit));
    }

    /**
     * 解锁
     *
     * @param lockKey 锁的key
     * @method unLock
     * @author krasus1966
     * @date 2022/10/27 19:56
     * @description 解锁
     */
    public static void unLock(String lockKey) {
        stringRedisTemplate.delete(lockKey);
    }
}
