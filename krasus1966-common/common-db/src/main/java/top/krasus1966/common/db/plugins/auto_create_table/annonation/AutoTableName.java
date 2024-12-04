package top.krasus1966.common.db.plugins.auto_create_table.annonation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标注-自动构建表结构-表名
 *
 * @author krasus1966
 * @date 2024/9/12 15:53
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Component
public @interface AutoTableName {

    /**
     * 数据库名称
     *//*
    String catalog();*/

    /**
     * 表名
     */
    String value();

    /**
     * 注释
     */
    String comment() default "";
}
