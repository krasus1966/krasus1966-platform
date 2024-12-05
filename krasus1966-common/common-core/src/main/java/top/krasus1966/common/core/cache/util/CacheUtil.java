package top.krasus1966.common.core.cache.util;

import com.fasterxml.jackson.core.type.TypeReference;
import top.krasus1966.common.core.cache.CacheFactory;
import top.krasus1966.common.core.cache.ICache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author krasus1966
 * @date 2024/12/5 10:59
 **/
public class CacheUtil {

    private static ICache cache = CacheFactory.getCache();


    public static boolean keyIsExist(String key) {
        return cache.keyIsExist(key);
    }


    public static Long ttl(String key) {
        return cache.ttl(key);
    }


    public static void expire(String key, long timeout) {
        cache.expire(key, timeout);
    }


    public static Long increment(String key, long delta) {
        return cache.increment(key, delta);
    }


    public static Long decrement(String key, long delta) {
        return cache.decrement(key, delta);
    }


    public static void del(String key) {
        cache.del(key);
    }


    public static void set(String key, String value) {
        cache.set(key, value);
    }


    public static void set(String key, String value, long timeout) {
        cache.set(key, value, timeout);
    }


    public static void set(String key, String value, Long timeout, TimeUnit unit) {
        cache.set(key, value, timeout, unit);
    }


    public static void setObject(String key, Object value) {
        cache.setObject(key, value);
    }


    public static void setObject(String key, Object value, long timeout) {
        cache.setObject(key, value, timeout);
    }


    public static void setObject(String key, Object value, Long time, TimeUnit unit) {
        cache.setObject(key, value, time, unit);
    }


    public static void setnx60s(String key, String value) {
        cache.setnx60s(key, value);
    }


    public static void setnx(String key, String value) {
        cache.setnx(key, value);
    }


    public static void setnx(String key, String value, int second) {
        cache.setnx(key, value, second);
    }


    public static String get(String key) {
        return cache.get(key);
    }


    public static <T> T getObject(String key, Class<T> type) {
        return cache.getObject(key, type);
    }


    public static <T> T getObject(String key, TypeReference<T> type) {
        return cache.getObject(key, type);
    }


    public static void hset(String key, String field, String value) {
        cache.hset(key, field, value);
    }


    public static void hset(String key, String field, String value, Long time) {
        cache.hset(key, field, value, time);
    }


    public static void hset(String key, String field, Object value) {
        cache.hset(key, field, value);
    }


    public static void hset(String key, Map<String, String> value, Long time) {
        cache.hset(key, value, time);
    }


    public static void hset(String key, Map<String, String> value) {
        cache.hset(key, value);
    }


    public static void hset(String key, String field, Object value, Long time) {
        cache.hset(key, field, value, time);
    }


    public static Map<String, String> hget(String key) {
        return cache.hget(key);
    }


    public static <T> T hget(String key, T value) {
        return cache.hget(key, value);
    }


    public static String hget(String key, String field) {
        return cache.hget(key, field);
    }


    public static <T> T hget(String key, String field, Class<T> type) {
        return cache.hget(key, field, type);
    }


    public static void hdel(String key, String field) {
        cache.hdel(key, field);
    }


    public static boolean trySimpleLock(String key, long timeoutSec) {
        return cache.tryLock(key, timeoutSec);
    }


    public static void unlock(String key) {
        cache.unlock(key);
    }
}
