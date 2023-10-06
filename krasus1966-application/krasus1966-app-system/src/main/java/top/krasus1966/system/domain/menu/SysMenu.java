package top.krasus1966.system.domain.menu;


import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.cache.constant.DictConst;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.valid.anno.group.Insert;
import top.krasus1966.valid.anno.group.Update;

import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author krasus1966
 * @since 2022-01-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel(value = "SysMenuEntity对象", description = "菜单表")
public class SysMenu extends AbstractPersistent {


    public static final String PARENT_ID = "parent_id";
    public static final String MENU_CODE = "menu_code";
    public static final String MENU_TITLE = "menu_title";
    public static final String MENU_NAME = "menu_name";
    public static final String MENU_ROUTE = "menu_route";
    public static final String MENU_ICON = "menu_icon";
    public static final String MENU_TYPE = "menu_type";
    public static final String IS_HIDDEN = "is_hidden";
    @ApiModelProperty("父菜单id")
    @TableField("parent_id")
    @NotBlank(message = "父菜单id不能为空", groups = {Insert.class, Update.class})
    private String parentId;

    @ApiModelProperty("菜单编码")
    @TableField("menu_code")
    @NotBlank(message = "菜单编码不能为空", groups = {Insert.class, Update.class})
    private String menuCode;

    @ApiModelProperty("菜单名称")
    @TableField("menu_title")
    @NotBlank(message = "菜单名称不能为空", groups = {Insert.class, Update.class})
    private String menuTitle;
    @ApiModelProperty("菜单name")
    @TableField("menu_name")
    @NotBlank(message = "菜单name不能为空", groups = {Insert.class, Update.class})
    private String menuName;

    @ApiModelProperty("菜单路由")
    @TableField("menu_route")
    @NotBlank(message = "菜单路由不能为空", groups = {Insert.class, Update.class})
    private String menuRoute;


    @ApiModelProperty("菜单图标")
    @TableField("menu_icon")
    @NotBlank(message = "菜单图标不能为空", groups = {Insert.class, Update.class})
    private String menuIcon;

    @ApiModelProperty("菜单类型 页面/按钮 CDLX")
    @TableField("menu_type")
    @NotBlank(message = "菜单类型不能为空", groups = {Insert.class, Update.class})
    private String menuType;

    @ApiModelProperty("菜单是否隐藏 字典SF")
    @NotBlank(message = "是否隐藏不能为空", groups = {Insert.class, Update.class})
    @TableField("is_hidden")
    private String isHidden;

    @ApiModelProperty("菜单是否前端缓存 字典SF")
    @TableField("no_cache")
    private String noCache;

    @ApiModelProperty("排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "逻辑删除 SCZT", hidden = true)
    @TableField(value = "deleted",fill = FieldFill.INSERT)
    @TableLogic
    private String deleted;

    @ApiModelProperty("子菜单列表")
    @TableField(exist = false)
    private List<SysMenu> children;

    @ApiModelProperty("层级")
    @TableField(exist = false)
    private Integer level;

    public SysMenu setChildren(List<SysMenu> children) {
        this.children = children;
        return this;
    }

    /**
     * 通用查询条件封装
     *
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<top.krasus1966.base.domain.SysMenu>
     * @method getBaseWrapper
     * @author krasus1966
     * @date 2022/1/8 19:51
     * @description 通用查询条件封装
     */
    public QueryWrapper<SysMenu> createWrapper() {
        return new QueryWrapper<SysMenu>()
                .eq(CharSequenceUtil.isNotBlank(parentId), PARENT_ID, parentId)
                .eq(CharSequenceUtil.isNotBlank(menuCode), MENU_CODE, menuCode)
                .eq(CharSequenceUtil.isNotBlank(menuTitle), MENU_TITLE, menuTitle)
                .like(CharSequenceUtil.isNotBlank(menuName), MENU_NAME, menuName)
                .eq(CharSequenceUtil.isNotBlank(menuType), MENU_TYPE, menuType)
                .eq(Constants.Deleted.DELETED, DictConst.DELETE_TYPE.DELETE_TYPE_NO);
    }
}
