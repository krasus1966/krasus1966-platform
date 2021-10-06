package top.krasus1966.swagger.anno;

import java.lang.annotation.*;

/**
 * 标注被Swagger扫描的类
 *
 * @author Krasus1966
 * @date 2021/9/23 23:47
 **/
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SwaggerDoc {
}
