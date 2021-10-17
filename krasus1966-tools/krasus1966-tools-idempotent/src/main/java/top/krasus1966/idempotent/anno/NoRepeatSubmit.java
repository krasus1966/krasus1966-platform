package top.krasus1966.idempotent.anno;

import java.lang.annotation.*;

/**
 * 避免重复提交注解 标注在方法上，锁未删除时禁止重复请求
 *
 * @author Krasus1966
 * @date 2021/6/15 08:49
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface NoRepeatSubmit {

    /**
     * 加锁时间
     *
     * @return
     */
    int lockTime() default -1;
}
