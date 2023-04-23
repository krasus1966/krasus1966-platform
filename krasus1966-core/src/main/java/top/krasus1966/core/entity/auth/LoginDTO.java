package top.krasus1966.core.entity.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Krasus1966
 * @date 2022/10/31 14:40
 **/
@Data
public class LoginDTO implements Serializable {

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
}
