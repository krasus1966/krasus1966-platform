package top.krasus1966.core.entity.auth;

import lombok.Data;

/**
 * @author Krasus1966
 * @date 2021/5/17 15:01
 **/
@Data
public class LoginVO {

    private String token;
    private String newKeys;

    public LoginVO(String token, String newKeys) {
        this.token = token;
        this.newKeys = newKeys;
    }
}
