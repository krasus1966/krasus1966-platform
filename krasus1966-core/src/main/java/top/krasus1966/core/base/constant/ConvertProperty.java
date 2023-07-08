package top.krasus1966.core.base.constant;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import top.krasus1966.core.base.exception.BizException;

/**
 * @author Krasus1966
 * @date 2023/5/3 21:14
 **/
@ConfigurationProperties(prefix = "project.convert")
@Slf4j
@Validated
public class ConvertProperty {
    /**
     * 是否启用ffmpeg，若未安装无法设置为true
     */
    private boolean ffmpeg = false;

//    @NotBlank(message = "本系统文件下载地址不能为空")
    private String downloadUrl;

    /**
     * 临时文件目录
     */
//    @NotBlank(message = "临时文件目录不能为空")
    private String tempFilePath;

    /**
     * 转码文件目录
     */
//    @NotBlank(message = "视频转码目录不能为空")
    private String videoTransPath;

    /**
     * 附加文件目录
     */
//    @NotBlank(message = "附加文件目录不能为空")
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
    public ConvertProperty setFfmpeg(boolean ffmpeg) {
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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public String getVideoTransPath() {
        return videoTransPath;
    }

    public void setVideoTransPath(String videoTransPath) {
        this.videoTransPath = videoTransPath;
    }

    public String getExtraFilePath() {
        return extraFilePath;
    }

    public void setExtraFilePath(String extraFilePath) {
        this.extraFilePath = extraFilePath;
    }

//    @PostConstruct
    public void init() {
        String ffmpegInfo = RuntimeUtil.execForStr("ffmpeg -version");
        if (!ffmpegInfo.contains("version")) {
            log.warn("未安装ffmpeg，或ffmpeg未配置环境变量内，转码功能将无法使用");
        } else {
            if (this.ffmpeg) {
                log.info("已安装ffmpeg");
            } else {
                log.info("已安装ffmpeg但未启用,转码功能将无法使用");
            }
        }
        if (CharSequenceUtil.isBlank(tempFilePath)) {
            throw new BizException("本系统文件下载地址不能为空");
        }
        if (CharSequenceUtil.isBlank(tempFilePath)) {
            log.error("临时文件目录不能为空");
            System.exit(1);
        }
        if (this.ffmpeg) {
            if (CharSequenceUtil.isBlank(videoTransPath)) {
                log.error("视频转码临时文件地址不能为空");
                System.exit(1);
            }
            if (CharSequenceUtil.isBlank(extraFilePath)) {
                log.error("视频转码附加临时文件地址不能为空");
                System.exit(1);
            }
        }
    }
}
