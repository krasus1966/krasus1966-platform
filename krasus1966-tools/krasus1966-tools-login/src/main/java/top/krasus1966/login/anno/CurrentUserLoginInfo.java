package top.krasus1966.login.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定登录用户信息
 *
 * @author Krasus1966
 * @date 2021/9/27 23:01
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserLoginInfo {
}
