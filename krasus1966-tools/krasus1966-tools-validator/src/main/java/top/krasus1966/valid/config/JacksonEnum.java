package top.krasus1966.valid.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 单例实现
 *
 * @author Krasus1966
 * @date 2022/4/15 21:43
 **/
public enum JacksonEnum {
    /**
     * 获取实例
     */
    INSTANCE;

    private final ObjectMapper objectMapper;

    JacksonEnum() {
        this.objectMapper = new Jackson2ObjectMapperBuilder()
                .findModulesViaServiceLoader(true)
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
