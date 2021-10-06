package top.krasus1966.valid.anno;

import org.springframework.context.annotation.Import;
import top.krasus1966.valid.config.ValidExceptionAdvice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Krasus1966
 * @date 2021/9/26 09:10
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ValidExceptionAdvice.class)
public @interface EnableGlobalAdvice {
}
