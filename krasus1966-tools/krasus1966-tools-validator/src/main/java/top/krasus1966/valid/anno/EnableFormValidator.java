package top.krasus1966.valid.anno;

import org.springframework.context.annotation.Import;
import top.krasus1966.valid.config.ValidatorConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启快速失败返回模式
 *
 * @author Krasus1966
 * @date 2021/9/26 00:18
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ValidatorConfiguration.class)
public @interface EnableFormValidator {
}
