package top.krasus1966.system.domain.sys_user;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.base.constant.DateTimeConstants;
import top.krasus1966.core.cache.constant.DictConst;
import top.krasus1966.core.crypto.anno.FieldCrypto;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.system.domain.menu.SysMenu;
import top.krasus1966.valid.anno.group.Insert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:42
 **/
@TableName("sys_user")
@Data
public class SysUser extends AbstractPersistent {

    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String NICK_NAME = "nick_name";
    public static final String REAL_NAME = "real_name";
    public static final String AVATAR = "avatar";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String ID_CARD = "id_card";
    public static final String SEX = "sex";
    public static final String BIRTHDAY = "birthday";
    public static final String ORG_ID = "org_id";
    public static final String DEPT_ID = "dept_id";
    public static final String AUTH_TYPE = "auth_type";
    public static final String STATUS = "status";
    public static final String VERSION = "version";
    @ApiModelProperty("用户名")
    @TableField(value = "user_name", condition = SqlCondition.LIKE)
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @ApiModelProperty("密码")
    @TableField(value = "password")
    @NotBlank(message = "密码不能为空", groups = Insert.class)
    private String password;
    @ApiModelProperty("昵称")
    @TableField(value = "nick_name", condition = SqlCondition.LIKE)
    @NotBlank(message = "昵称不能为空", groups = Insert.class)
    private String nickName;
    @ApiModelProperty("真实姓名")
    @TableField(value = "real_name", condition = SqlCondition.LIKE)
    @NotBlank(message = "真实姓名不能为空", groups = Insert.class)
    private String realName;
    @ApiModelProperty("头像")
    @TableField(value = "avatar")
    @NotBlank(message = "头像不能为空", groups = Insert.class)
    private String avatar;
    @ApiModelProperty("手机号")
    @TableField(value = "mobile")
    @NotBlank(message = "手机号不能为空", groups = Insert.class)
    @FieldCrypto
    private String mobile;
    @ApiModelProperty("邮箱地址")
    @TableField(value = "email")
    @NotBlank(message = "邮箱地址不能为空", groups = Insert.class)
    private String email;
    @ApiModelProperty("身份证号")
    @TableField(value = "id_card")
    @NotBlank(message = "身份证号不能为空", groups = Insert.class)
    private String idCard;
    @ApiModelProperty("性别 字典")
    @TableField(value = "sex")
    private String sex;
    @ApiModelProperty("生日")
    @TableField(value = "birthday")
    @DateTimeFormat(pattern = DateTimeConstants.DATE_FORMAT_STANDARD)
    @JsonFormat(pattern = DateTimeConstants.DATE_FORMAT_STANDARD, timezone = "GMT+8")
    private LocalDate birthday;
    @ApiModelProperty("组织id")
    @TableField(value = "org_id")
    private String orgId;
    @ApiModelProperty("部门id")
    @TableField(value = "dept_id")
    private String deptId;
    @ApiModelProperty("权限类型 QXLX")
    @TableField(value = "auth_type")
    @NotBlank(message = "权限类型不能为空", groups = {Insert.class})
    private String authType;
    @ApiModelProperty("最后一次登录角色id")
    @TableField(value = "last_login_role_id")
    private String lastLoginRoleId;
    @ApiModelProperty("盐值")
    @TableField(value = "salt")
    private String salt;
    @ApiModelProperty("状态 ZT")
    @TableField(value = "status")
    @NotBlank(message = "状态不能为空", groups = {Insert.class})
    private String status;
    @ApiModelProperty("乐观锁")
    @TableField(value = "version", update = "%s+1")
    @Version
    private Integer version;
    @ApiModelProperty("逻辑删除 SCZT")
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private String deleted;
    /**
     * 菜单数据
     */
    @TableField(exist = false)
    private List<SysMenu> menus = new ArrayList<>();
    /**
     * 按钮数据
     */
    @TableField(exist = false)
    private String buttons = "";
    /**
     * 附加信息
     */
    @TableField(exist = false)
    private Map<String, String> infoMap = new HashMap<>();
    /**
     * 角色列表
     */
    @TableField(exist = false)
    private List<SysUserRole> roleList = new ArrayList<>();

    /**
     * 检查密码与当前用户是否相同
     *
     * @param pwd 前台传递密码
     * @return boolean
     * @method checkPassword
     * @author krasus1966
     * @date 2022/11/17 21:03
     * @description 检查密码与当前用户是否相同
     */
    public boolean checkPassword(String pwd) {
        return pwd.equals(this.password);
    }

    /**
     * 通用QueryWrapper
     *
     * @param
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<top.krasus1966.base.domain.SysUser>
     * @method getBaseWrapper
     * @author krasus1966
     * @date 2022/3/3 22:17
     * @description 通用QueryWrapper
     */
    public Wrapper<SysUser> createWrapper() {
        return new QueryWrapper<SysUser>()
                .like(CharSequenceUtil.isNotBlank(userName), USER_NAME, userName)
                .like(CharSequenceUtil.isNotBlank(nickName), NICK_NAME, nickName)
                .like(CharSequenceUtil.isNotBlank(realName), REAL_NAME, realName)
                .eq(CharSequenceUtil.isNotBlank(mobile), MOBILE, mobile)
                .eq(CharSequenceUtil.isNotBlank(email), EMAIL, email)
                .eq(CharSequenceUtil.isNotBlank(idCard), ID_CARD, idCard)
                .eq(CharSequenceUtil.isNotBlank(sex), SEX, sex)
                .eq(CharSequenceUtil.isNotBlank(orgId), ORG_ID, orgId)
                .eq(CharSequenceUtil.isNotBlank(deptId), DEPT_ID, deptId)
                .eq(CharSequenceUtil.isNotBlank(authType), AUTH_TYPE, authType)
                .eq(CharSequenceUtil.isNotBlank(status), STATUS, status)
                .eq(Constants.Deleted.DELETED, DictConst.DELETE_TYPE.DELETE_TYPE_NO)
                ;
    }

    /**
     * 权限类型
     */
    public interface AuthType {
        /**
         * 普通用户
         */
        String NORMAL_USER = "normal";
        /**
         * 管理员用户
         */
        String ADMIN_USER = "admin";
    }
}
