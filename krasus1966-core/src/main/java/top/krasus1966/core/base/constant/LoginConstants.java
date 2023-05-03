package top.krasus1966.core.base.constant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.krasus1966.core.crypto.util.CryptoType;


/**
 * 外部配置
 *
 * @author Krasus1966
 * @date 2022/10/31 13:24
 **/
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

    public String getHeaderUserToken() {
        return headerUserToken;
    }

    public LoginConstants setHeaderUserToken(String headerUserToken) {
        this.headerUserToken = headerUserToken;
        return this;
    }

    public String getHeaderSignName() {
        return headerSignName;
    }

    public LoginConstants setHeaderSignName(String headerSignName) {
        this.headerSignName = headerSignName;
        return this;
    }

    public String getHeaderSignRandomName() {
        return headerSignRandomName;
    }

    public LoginConstants setHeaderSignRandomName(String headerSignRandomName) {
        this.headerSignRandomName = headerSignRandomName;
        return this;
    }

    public String getHeaderSignAesKey() {
        return headerSignAesKey;
    }

    public LoginConstants setHeaderSignAesKey(String headerSignAesKey) {
        this.headerSignAesKey = headerSignAesKey;
        return this;
    }

    public Long getExpireTimeLogin() {
        return expireTimeLogin;
    }

    public LoginConstants setExpireTimeLogin(Long expireTimeLogin) {
        this.expireTimeLogin = expireTimeLogin;
        return this;
    }

    public Integer getFailLockNum() {
        return failLockNum;
    }

    public LoginConstants setFailLockNum(Integer failLockNum) {
        this.failLockNum = failLockNum;
        return this;
    }

    public Long getLockLoginExpire() {
        return lockLoginExpire;
    }

    public LoginConstants setLockLoginExpire(Long lockLoginExpire) {
        this.lockLoginExpire = lockLoginExpire;
        return this;
    }

    public Long getTimeoutExpire() {
        return timeoutExpire;
    }

    public LoginConstants setTimeoutExpire(Long timeoutExpire) {
        this.timeoutExpire = timeoutExpire;
        return this;
    }

    public CryptoType.SignCryptoType getSignCryptoType() {
        return signCryptoType;
    }

    public LoginConstants setSignCryptoType(CryptoType.SignCryptoType signCryptoType) {
        if (null == signCryptoType) {
            log.error("必须设置签名加密算法");
            System.exit(1);
        }
        this.signCryptoType = signCryptoType;
        return this;
    }
}
