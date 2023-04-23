package top.krasus1966.core.constant;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import top.krasus1966.core.util.crypto.CryptoType;

import javax.validation.constraints.NotBlank;


/**
 * 外部配置
 *
 * @author Krasus1966
 * @date 2022/10/31 13:24
 **/
@ConfigurationProperties(prefix = "project.properties")
@Slf4j
@Validated
public class PropertiesConstants {

    /**
     * 是否启用ffmpeg，若未安装无法设置为true
     */
    private boolean ffmpeg = false;

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

    /**
     * 临时文件目录
     */
    @NotBlank(message = "临时文件目录不能为空")
    private String tempFilePath;

    /**
     * 转码文件目录
     */
    @NotBlank(message = "视频转码目录不能为空")
    private String videoTransPath;

    /**
     * 附加文件目录
     */
    @NotBlank(message = "附加文件目录不能为空")
    private String extraFilePath;

    public boolean getFfmpeg() {
        return ffmpeg;
    }

    /**
     * 检查是否安装ffmpeg，未安装则产生错误提示，且转码功能不可用
     *
     * @return top.krasus1966.base.common.properties.TempFileProperties
     * @method setFfmpeg
     * @author krasus1966
     * @date 2022/4/19 22:51
     * @description 检查是否安装ffmpeg，未安装则产生错误提示，且转码功能不可用
     */
    public PropertiesConstants setFfmpeg(boolean ffmpeg) {
        // 判断是否安装ffmpeg
        String ffmpegInfo = RuntimeUtil.execForStr("ffmpeg -version");
        if (!ffmpegInfo.contains("version")) {
            log.warn("未安装ffmpeg，或ffmpeg未配置环境变量内，转码功能将无法使用");
            this.ffmpeg = false;
        } else {
            this.ffmpeg = ffmpeg;
            if (this.ffmpeg) {
                log.info("已安装ffmpeg");
            } else {
                log.info("已安装ffmpeg但未启用,转码功能将无法使用");
            }
        }
        return this;
    }

    public String getHeaderUserToken() {
        return headerUserToken;
    }

    public PropertiesConstants setHeaderUserToken(String headerUserToken) {
        this.headerUserToken = headerUserToken;
        return this;
    }

    public String getHeaderSignName() {
        return headerSignName;
    }

    public PropertiesConstants setHeaderSignName(String headerSignName) {
        this.headerSignName = headerSignName;
        return this;
    }

    public String getHeaderSignRandomName() {
        return headerSignRandomName;
    }

    public PropertiesConstants setHeaderSignRandomName(String headerSignRandomName) {
        this.headerSignRandomName = headerSignRandomName;
        return this;
    }

    public String getHeaderSignAesKey() {
        return headerSignAesKey;
    }

    public PropertiesConstants setHeaderSignAesKey(String headerSignAesKey) {
        this.headerSignAesKey = headerSignAesKey;
        return this;
    }

    public Long getExpireTimeLogin() {
        return expireTimeLogin;
    }

    public PropertiesConstants setExpireTimeLogin(Long expireTimeLogin) {
        this.expireTimeLogin = expireTimeLogin;
        return this;
    }

    public Integer getFailLockNum() {
        return failLockNum;
    }

    public PropertiesConstants setFailLockNum(Integer failLockNum) {
        this.failLockNum = failLockNum;
        return this;
    }

    public Long getLockLoginExpire() {
        return lockLoginExpire;
    }

    public PropertiesConstants setLockLoginExpire(Long lockLoginExpire) {
        this.lockLoginExpire = lockLoginExpire;
        return this;
    }

    public Long getTimeoutExpire() {
        return timeoutExpire;
    }

    public PropertiesConstants setTimeoutExpire(Long timeoutExpire) {
        this.timeoutExpire = timeoutExpire;
        return this;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public PropertiesConstants setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
        return this;
    }

    public String getVideoTransPath() {
        return videoTransPath;
    }

    public PropertiesConstants setVideoTransPath(String videoTransPath) {
        if (this.ffmpeg) {
            if (CharSequenceUtil.isBlank(videoTransPath)) {
                log.error("视频转码临时文件地址不能为空");
                System.exit(1);
            }
        }
        this.videoTransPath = videoTransPath;
        return this;
    }

    public String getExtraFilePath() {
        return extraFilePath;
    }

    public PropertiesConstants setExtraFilePath(String extraFilePath) {
        if (this.ffmpeg) {
            if (CharSequenceUtil.isBlank(extraFilePath)) {
                log.error("视频转码附加临时文件地址不能为空");
                System.exit(1);
            }
        }
        this.extraFilePath = extraFilePath;
        return this;
    }

    public CryptoType.SignCryptoType getSignCryptoType() {
        return signCryptoType;
    }

    public PropertiesConstants setSignCryptoType(CryptoType.SignCryptoType signCryptoType) {
        if (null == signCryptoType) {
            log.error("必须设置签名加密算法");
            System.exit(1);
        }
        this.signCryptoType = signCryptoType;
        return this;
    }
}
