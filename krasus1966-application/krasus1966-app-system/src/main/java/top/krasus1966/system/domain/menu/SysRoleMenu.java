package top.krasus1966.system.domain.menu;

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
 * 角色菜单表
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role_menu")
@ApiModel(value = "SysRoleMenuEntity对象", description = "角色菜单表")
@NoArgsConstructor
public class SysRoleMenu extends AbstractPersistent {

    @ApiModelProperty("角色id")
    @TableField("role_id")
    @NotBlank(message = "角色id不能为空", groups = {Insert.class, Update.class})
    private String roleId;

    @ApiModelProperty("菜单id")
    @TableField("menu_id")
    @NotBlank(message = "菜单id不能为空", groups = {Insert.class, Update.class})
    private String menuId;


    public static final String ROLE_ID = "role_id";

    public static final String MENU_ID = "menu_id";

    public SysRoleMenu(String roleId, String menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    public QueryWrapper<SysRoleMenu> createWrapper() {
        return new QueryWrapper<SysRoleMenu>()
                .eq(CharSequenceUtil.isNotBlank(roleId), ROLE_ID, roleId)
                .eq(CharSequenceUtil.isNotBlank(menuId), MENU_ID, menuId);
    }
}
