package top.krasus1966.core.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 图片验证码BO
 *
 * @author Krasus1966
 * @date 2021/9/19 23:24
 **/
@Data
@AllArgsConstructor
public class Captcha implements Serializable {
    private String token;
    private String base64Img;
}
