package top.krasus1966.core.web.auth.anno;

import java.lang.annotation.*;

/**
 * @author Krasus1966
 * @date 2022/11/1 08:44
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER})
public @interface LoginUser {
}
