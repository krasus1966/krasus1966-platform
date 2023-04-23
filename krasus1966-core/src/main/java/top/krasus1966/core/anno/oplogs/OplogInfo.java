package top.krasus1966.core.anno.oplogs;

import java.lang.annotation.*;

/**
 * 标注在方法上，记录操作内容
 *
 * @author Krasus1966
 * @date 2021/6/23 08:51
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OplogInfo {

    String value() default "";
}
