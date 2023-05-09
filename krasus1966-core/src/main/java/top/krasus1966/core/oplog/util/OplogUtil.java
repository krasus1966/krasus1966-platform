package top.krasus1966.core.oplog.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2023/5/3 17:58
 **/
public class OplogUtil {

    private OplogUtil() {
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public static Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public static String stackTraceToString(String exceptionName, String exceptionMessage,
                                            StackTraceElement[] elements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement stet : elements) {
            stringBuilder.append(stet).append("\n");
        }
        return exceptionName + ":" + exceptionMessage + "\n\t" + stringBuilder.toString();
    }
}
