package top.krasus1966.core.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.krasus1966.core.spring.util.SpringUtil;

/**
 * @author Krasus1966
 * @date 2023/4/26 23:30
 **/
public enum JacksonEnum {
    INSTANCE;


    private final ObjectMapper objectMapper;

    JacksonEnum() {
        this.objectMapper = SpringUtil.getBean(ObjectMapper.class);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
