package top.krasus1966.system.domain.menu;


import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.valid.anno.group.Insert;
import top.krasus1966.valid.anno.group.Update;


/**
 * <p>
 * 角色表
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value = "SysRoleEntity对象", description = "角色表")
public class SysRole extends AbstractPersistent {


    public static final String ROLE_NAME = "role_name";
    public static final String ROLE_TYPE = "role_type";
    public static final String STATUS = "status";
    public static final String CAN_CHANGE = "can_change";
    public static final String CAN_DELETE = "can_delete";
    public static final String COM_ID = "com_id";
    public static final String ORG_ID = "org_id";
    public static final String DEPT_ID = "dept_id";
    public static final String UNION_ID = "union_id";
    @ApiModelProperty("角色名称")
    @TableField("role_name")
    @NotBlank(message = "角色名称不能为空", groups = {Insert.class, Update.class})
    private String roleName;
    @ApiModelProperty("角色类型 字典 JSLX")
    @TableField("role_type")
    @NotBlank(message = "角色类型不能为空", groups = {Insert.class, Update.class})
    private String roleType;
    @ApiModelProperty("状态 字典 ZT")
    @TableField("status")
    @NotBlank(message = "状态不能为空", groups = {Insert.class, Update.class})
    private String status;
    @ApiModelProperty("是否可更改 字典 SF")
    @TableField("can_change")
    private String canChange;
    @ApiModelProperty("是否可删除 字典 SF")
    @TableField("can_delete")
    private String canDelete;
    @ApiModelProperty("菜单ids")
    @TableField(exist = false)
    private String menuIds;

    public QueryWrapper<SysRole> createWrapper() {
        return new QueryWrapper<SysRole>()
                .like(CharSequenceUtil.isNotBlank(roleName), ROLE_NAME, roleName)
                .eq(CharSequenceUtil.isNotBlank(roleType), ROLE_TYPE, roleType)
                .eq(CharSequenceUtil.isNotBlank(status), STATUS, status)
                .eq(CharSequenceUtil.isNotBlank(canChange), CAN_CHANGE, canChange)
                .eq(CharSequenceUtil.isNotBlank(canDelete), CAN_DELETE, canDelete);
    }

    /**
     * 默认角色id
     */
    public interface DEFAULT_ROLE_IDS {
        String SUPER_ADMIN = "SUPER_ADMIN";
        String EMPLOYEE = "EMPLOYEE";
    }
}
