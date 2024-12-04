package top.krasus1966.common.db.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author krasus1966
 * @date 2024/10/12 10:01
 **/
@Data
public class TablePermission implements Serializable {

    public static final String SJQX_ALL = "SJQX_ALL";
    public static final String SJQX_DEPT = "SJQX_DEPT";
    public static final String SJQX_DEPT_ALL = "SJQX_DEPT_ALL";
    public static final String SJQX_BR = "SJQX_BR";

    /**
     * 表名
     */
    private String tableName;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 权限类型
     */
    private String permissionType;
    /**
     * 创建人字段
     */
    private String creatorColumn;
    /**
     * 部门字段
     */
    private String departColumn;
    /**
     * 特殊规则
     */
    private String specialRules;
    /**
     * 排序
     */
    private Integer sort;
}
