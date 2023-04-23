package top.krasus1966.core.util.redis_util;

import java.util.Map;

/**
 * 字典Redis缓存工具类
 *
 * @author Krasus1966
 * @date 2022/11/18 22:46
 **/
public class DictionaryRedisUtil {

    private static final String DICT_CACHE_NAME = "DICT_CACHE:DICT_VALUE:";

    /**
     * 存储字典组
     *
     * @param dictCode code
     * @param dictMap  字典，
     *                 key：dictValue
     *                 value: dictName
     * @method saveDictMap
     * @author krasus1966
     * @date 2022/11/18 23:03
     * @description 存储字典组
     */
    public static void saveDictMap(String dictCode, Map<String, String> dictMap) {
        CacheUtil.hset(DICT_CACHE_NAME + dictCode, dictMap);
    }

    /**
     * 存储字典
     *
     * @param dictValue 字典值
     * @param dictName  字典名
     * @method saveDict
     * @author krasus1966
     * @date 2022/11/18 23:04
     * @description 存储字典
     */
    public static void saveDict(String dictCode, String dictValue, String dictName) {
        CacheUtil.hset(DICT_CACHE_NAME + dictCode, dictValue, dictName);
    }

    /**
     * 通过dictCode获取字典组
     *
     * @param dictCode code
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @method getDictMapByCode
     * @author krasus1966
     * @date 2022/11/18 23:09
     * @description 通过dictCode获取字典组
     */
    public static Map<String, String> getDictMapByCode(String dictCode) {
        return CacheUtil.hget(DICT_CACHE_NAME + dictCode);
    }

    /**
     * 通过dictCode和dictValue获取字典名
     *
     * @param dictCode  dictCode
     * @param dictValue dictValue
     * @return java.lang.String
     * @method getDictName
     * @author krasus1966
     * @date 2022/11/18 23:09
     * @description 通过dictCode和dictValue获取字典名
     */
    public static String getDictName(String dictCode, String dictValue) {
        return CacheUtil.hget(DICT_CACHE_NAME + dictCode, dictValue);
    }

    /**
     * 删除字典组
     *
     * @param dictCode code
     * @method removeByDictCode
     * @author krasus1966
     * @date 2022/11/18 23:16
     * @description 删除字典组
     */
    public static void removeByDictCode(String dictCode) {
        CacheUtil.del(DICT_CACHE_NAME + dictCode);
    }

    /**
     * 删除字典值
     *
     * @param dictCode  code
     * @param dictValue value
     * @return void
     * @throws
     * @method removeByDictCodeAndDictValue
     * @author krasus1966
     * @date 2022/11/18 23:16
     * @description 删除字典值
     */
    public static void removeByDictCodeAndDictValue(String dictCode, String dictValue) {
        CacheUtil.hdel(DICT_CACHE_NAME + dictCode, dictValue);
    }
}
