package top.krasus1966.core.web.auth.anno;

import java.lang.annotation.*;

/**
 * @author Krasus1966
 * @date 2021/11/15 19:13
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface Auth {

    boolean login() default false;

    boolean sign() default true;
}
