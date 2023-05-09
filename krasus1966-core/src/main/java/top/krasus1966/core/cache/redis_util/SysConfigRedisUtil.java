package top.krasus1966.core.cache.redis_util;

import com.fasterxml.jackson.core.JsonProcessingException;
import top.krasus1966.core.cache.entity.SysConfigRedis;

/**
 * @author Krasus1966
 * @date 2022/11/18 23:14
 **/
public class SysConfigRedisUtil {

    private static final String CONFIG_CACHE_OBJ = "CONFIG_CACHE:OBJ:";
    private static final String CONFIG_CACHE_NAME = "CONFIG_CACHE:CONFIG_NAME:";
    private static final String CONFIG_CACHE_CODE = "CONFIG_CACHE:CONFIG_CODE:";

    /**
     * 保存系统配置
     *
     * @param configRedis 系统配置
     * @throws JsonProcessingException
     * @method saveConfig
     * @author krasus1966
     * @date 2022/11/18 23:29
     * @description 保存系统配置
     */
    public static void saveConfig(SysConfigRedis configRedis) throws JsonProcessingException {
        CacheUtil.setObject(CONFIG_CACHE_OBJ + configRedis.getConfigCode(), configRedis);
        CacheUtil.set(CONFIG_CACHE_NAME + configRedis.getConfigName(),
                configRedis.getConfigValue());
        CacheUtil.set(CONFIG_CACHE_CODE + configRedis.getConfigCode(),
                configRedis.getConfigValue());
    }

    /**
     * 通过code获取系统配置
     *
     * @param configCode code
     * @return java.lang.String
     * @method getConfigByCode
     * @author krasus1966
     * @date 2022/11/18 23:30
     * @description 通过code获取系统配置
     */
    public static String getConfigByCode(String configCode) {
        return CacheUtil.get(CONFIG_CACHE_CODE + configCode);
    }

    /**
     * 通过name获取系统配置
     *
     * @param configName 配置名
     * @return java.lang.String
     * @method getConfigByName
     * @author krasus1966
     * @date 2022/11/18 23:31
     * @description 通过name获取系统配置
     */
    public static String getConfigByName(String configName) {
        return CacheUtil.get(CONFIG_CACHE_NAME + configName);
    }

    /**
     * 通过code获取整个配置对象
     *
     * @param configCode code
     * @return top.krasus1966.base_project.common.core.entity.SysConfigRedis
     * @throws JsonProcessingException
     * @method getConfigObject
     * @author krasus1966
     * @date 2022/11/18 23:31
     * @description 通过code获取整个配置对象
     */
    public static SysConfigRedis getConfigObject(String configCode) throws JsonProcessingException {
        return CacheUtil.getObject(CONFIG_CACHE_OBJ + configCode, SysConfigRedis.class);
    }
}
