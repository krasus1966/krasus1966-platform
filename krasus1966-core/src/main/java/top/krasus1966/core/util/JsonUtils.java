package top.krasus1966.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import top.krasus1966.core.util.spring.SpringUtil;

import java.util.List;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2020/11/2 21:53
 **/
@Slf4j
public class JsonUtils {
    private static final ObjectMapper MAPPER = SpringUtil.getBean(ObjectMapper.class);

    private JsonUtils() {
    }

    /**
     * 将对象转换成json字符串。
     *
     * @param data 对象
     * @return string json字符串
     */
    public static String objectToJson(Object data) throws JsonProcessingException {
        return MAPPER.writeValueAsString(data);
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return 对象
     */
    public static <T> T jsonToObject(String jsonData, Class<T> beanType) throws JsonProcessingException {
        return MAPPER.readValue(jsonData, beanType);
    }

    /**
     * 将json数据转换成pojo对象list
     *
     * @param jsonData json数据
     * @param beanType 对象类型
     * @return list
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) throws JsonProcessingException {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        return MAPPER.readValue(jsonData, javaType);
    }

    /**
     * 将json数据转换成map
     *
     * @param jsonData  json数据
     * @param keyType   key类型
     * @param valueType value类型
     * @return map
     */
    public static <K, V> Map<K, V> jsonToMap(String jsonData, Class<K> keyType,
                                             Class<V> valueType) throws JsonProcessingException {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(Map.class, keyType,
                valueType);
        return MAPPER.readValue(jsonData, javaType);
    }

    /**
     * 将json数据转换成JsonNode
     *
     * @param jsonData json数据
     * @return map
     */
    public static JsonNode jsonToMap(String jsonData) throws JsonProcessingException {
        return MAPPER.readTree(jsonData);
    }
}
