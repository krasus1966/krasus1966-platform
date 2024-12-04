package top.krasus1966.common.core.cache;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 *
 * @author Krasus1966
 * @date 2023/6/24 22:24
 **/
public interface ICache {
    /**
     * 判断key是否存在
     *
     * @param key key
     * @return boolean
     */
    boolean keyIsExist(String key);

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key
     * @return
     */
    Long ttl(String key);

    /**
     * 实现命令：expire 设置过期时间，单位秒
     *
     * @param key
     * @return
     */
    void expire(String key, long timeout);

    /**
     * 实现命令：increment key，增加key一次
     *
     * @param key
     * @return
     */
    Long increment(String key, long delta);

    /**
     * 实现命令：decrement key，减少key一次
     *
     * @param key
     * @return
     */
    Long decrement(String key, long delta);

    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    void del(String key);

    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     * @param timeout （以秒为单位）
     */
    void set(String key, String value, long timeout);

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     * @param timeout
     */
    void set(String key, String value, Long timeout, TimeUnit unit);

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     */
    void setObject(String key, Object value);

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     * @param timeout （以秒为单位）
     */
    void setObject(String key, Object value, long timeout);

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
    void setObject(String key, Object value, Long time, TimeUnit unit);

    /**
     * 如果key不存在，则设置，如果存在，则报错
     *
     * @param key
     * @param value
     */
    void setnx60s(String key, String value);

    /**
     * 如果key不存在，则设置，如果存在，则报错
     *
     * @param key
     * @param value
     */
    void setnx(String key, String value);

    /**
     * 如果key不存在，则设置，如果存在，则报错
     *
     * @param key
     * @param value
     */
    void setnx(String key, String value, int second);

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     *
     * @param key
     * @return value
     */
    String get(String key);

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     *
     * @param key
     * @return value
     */
    <T> T getObject(String key, Class<T> type);

    <T> T getObject(String key, TypeReference<T> type);

    // Hash（哈希表）

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    void hset(String key, String field, String value);

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    void hset(String key, String field, String value, Long time) ;
    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    void hset(String key, String field, Object value);

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     * 直接存储整个对象，对象按 property:value 方式生成map再存储
     *
     * @param key
     * @param value
     */
     void hset(String key, Map<String, String> value, Long time);

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     * 直接存储整个对象，对象按 property:value 方式生成map再存储
     *
     * @param key
     * @param value
     */
    void hset(String key, Map<String, String> value);

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    void hset(String key, String field, Object value, Long time);

    Map<String, String> hget(String key);

     <T> T hget(String key, T value);

    String hget(String key, String field);

    <T> T hget(String key, String field, Class<T> type);

    void hdel(String key, String field);


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
    boolean trySimpleLock(String key, long timeoutSec);

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
    void unlock(String key);
}
