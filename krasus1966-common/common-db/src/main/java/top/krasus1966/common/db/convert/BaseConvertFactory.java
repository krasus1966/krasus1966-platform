package top.krasus1966.common.db.convert;

import java.util.HashMap;
import java.util.Map;

public class BaseConvertFactory {
    private static final Map<String, BaseConvert> CONVERT_MAP = new HashMap<>();

    public static <Source, Target> BaseConvert<Source, Target> getConvert(Class<Source> sourceClass, Class<Target> targetClass) {
        String key = sourceClass.getSimpleName() + "-" + targetClass.getSimpleName();
        if (CONVERT_MAP.containsKey(key)) {
            return CONVERT_MAP.get(key);
        }
        BaseConvert<Source, Target> convert = new BaseConvert<>(sourceClass, targetClass);
        CONVERT_MAP.put(key, convert);
        return convert;
    }
}
