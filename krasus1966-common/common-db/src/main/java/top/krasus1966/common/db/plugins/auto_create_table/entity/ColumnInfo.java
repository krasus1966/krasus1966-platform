package top.krasus1966.common.db.plugins.auto_create_table.entity;

import lombok.Data;
import org.apache.ibatis.type.JdbcType;

/**
 * 字段信息
 *
 * @author krasus1966
 * @date 2024/9/12 18:08
 **/
@Data
public class ColumnInfo {

    /**
     * 是否主键
     */
    private boolean primaryKey;

    /**
     * 是否自增
     */
    private boolean autoIncrement;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段长度
     */
    private int length;

    /**
     * 是否可以为空
     */
    private String nullable;

    /**
     * 注释
     */
    private String remarks;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 小数点
     */
    private int scale;

    /**
     * 数据类型
     */
    private JdbcType jdbcType;

    /**
     * 表名
     */
    private String table;

    /**
     * 表注释
     */
    private String notes;

    /**
     * 字段Java类名
     */
    private String javaClassName;

    /**
     * 是否为新加字段
     **/
    private boolean addField = true;

    /**
     * 数据库字段类型
     **/
    private String dbType;
}
