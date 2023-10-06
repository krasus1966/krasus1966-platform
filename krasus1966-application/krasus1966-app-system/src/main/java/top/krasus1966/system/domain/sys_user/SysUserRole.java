package top.krasus1966.system.domain.sys_user;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.valid.anno.group.Insert;
import top.krasus1966.valid.anno.group.Update;


/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_role")
@ApiModel(value = "SysUserRoleEntity对象", description = "用户角色表")
@NoArgsConstructor
public class SysUserRole extends AbstractPersistent {

    @ApiModelProperty("用户id")
    @TableField("user_id")
    @NotBlank(message = "用户id不能为空", groups = {Insert.class, Update.class})
    private String userId;

    @ApiModelProperty("角色id")
    @TableField("role_id")
    @NotBlank(message = "角色id不能为空", groups = {Insert.class, Update.class})
    private String roleId;

    public static final String USER_ID = "user_id";

    public static final String ROLE_ID = "role_id";

    public SysUserRole(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
    

    public QueryWrapper<SysUserRole> createWrapper() {
        return new QueryWrapper<SysUserRole>()
                .eq(CharSequenceUtil.isNotBlank(userId), USER_ID, userId)
                .eq(CharSequenceUtil.isNotBlank(roleId), ROLE_ID, roleId)
                ;
    }
}
