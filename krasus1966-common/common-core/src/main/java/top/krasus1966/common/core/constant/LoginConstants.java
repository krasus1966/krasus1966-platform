package top.krasus1966.common.core.constant;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.krasus1966.common.core.crypto.util.CryptoType;


/**
 * 外部配置
 *
 * @author Krasus1966
 * @date 2022/10/31 13:24
 **/
@Data
@ConfigurationProperties(prefix = "project.login")
@Slf4j
public class LoginConstants {

    /**
     * Header中用户信息key
     */
    private String headerUserToken = "authToken";

    /**
     * Header中签名key
     */
    private String headerSignName = "signature";

    /**
     * Header中签名随机字符串key
     */
    private String headerSignRandomName = "random";

    /**
     * Header中用户AESkey
     */
    private String headerSignAesKey = "newSignKey";

    /**
     * 缓存登录用户时间-秒
     */
    private Long expireTimeLogin = 3600L;

    /**
     * 失败锁定次数
     */
    private Integer failLockNum = 5;

    /**
     * 锁定时长-秒
     */
    private Long lockLoginExpire = 5 * 60L;

    /**
     * 过期时间
     */
    private Long timeoutExpire = 30 * 24 * 60 * 60L;

    /**
     * 签名加密算法
     */
    private CryptoType.SignCryptoType signCryptoType = CryptoType.SignCryptoType.SHA_256;

    public LoginConstants setSignCryptoType(CryptoType.SignCryptoType signCryptoType) {
        if (null == signCryptoType) {
            log.error("必须设置签名加密算法");
            System.exit(1);
        }
        this.signCryptoType = signCryptoType;
        return this;
    }
}
