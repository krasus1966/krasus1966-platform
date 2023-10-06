package top.krasus1966.core.web.auth.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Krasus1966
 * @date 2022/10/31 14:40
 **/
@Data
public class LoginDTO implements Serializable {

    public static final String USER_NAME = "username";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";

    /**
     * 账户 用户名/手机号码/邮箱
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 前端对称密钥
     */
    private String keys;

    /**
     * 验证码
     */
    private String code;

    /**
     * 登录方式
     */
    private String loginType;
}
