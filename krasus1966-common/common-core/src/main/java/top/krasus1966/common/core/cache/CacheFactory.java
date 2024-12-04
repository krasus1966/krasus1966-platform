package top.krasus1966.common.core.cache;

/**
 * 缓存工厂类
 *
 * @author daijiaqi
 * @version 1.0
 * @date 2022/5/13
 */
public class CacheFactory {

    /**
     * 缓存类
     */
    private volatile static ICache redisCache;

    private CacheFactory() {
    }

    /**
     * 获取缓存对象
     *
     * @return 缓存对象
     */
    public static final ICache getCache() {

        return redisCache;
    }
}
