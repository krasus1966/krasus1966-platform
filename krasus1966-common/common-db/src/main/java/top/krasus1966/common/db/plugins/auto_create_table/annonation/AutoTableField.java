package top.krasus1966.common.db.plugins.auto_create_table.annonation;

import java.lang.annotation.*;

/**
 * 标注-自动构建表结构-字段
 *
 * @author krasus1966
 * @date 2024/9/12 15:56
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface AutoTableField {

    String value() default "";

    boolean isPrimaryKey() default false;

    /**
     * 字段长度 字符串的情况下，长度超过300，设置为longtext
     */
    int length() default 64;

    /***
     * 小数点
     */
    int scale() default 0;

    /**
     * 注释
     */
    String comment() default "";// 备注

    /**
     * 默认值
     */
    String defaultValue() default "NULL";// 数据库默认值

    /**
     * 是否允许为空
     */
    boolean nullable() default true;// 能否为空
}
