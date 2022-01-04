package top.krasus1966.idempotent.anno;

import org.springframework.context.annotation.Import;
import top.krasus1966.idempotent.config.IdempotentConfig;

import java.lang.annotation.*;

/**
 * @author Krasus1966
 * @date 2021/11/6 14:31
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Import(IdempotentConfig.class)
public @interface EnableRepeatSubmit {
}
