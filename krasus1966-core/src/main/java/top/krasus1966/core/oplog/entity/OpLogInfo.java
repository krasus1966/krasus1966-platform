package top.krasus1966.core.oplog.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Krasus1966
 * @date 2022/4/17 21:33
 **/
@Data
@Document("oplog_info_#{T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter)" +
        ".ofPattern(\"yyyyMM\"))}")
public class OpLogInfo {
    @Id
    private String id;

    /**
     * 通用状态返回
     */
    private Integer resultStatus;
    /**
     * 通用消息返回
     */
    private String resultMessage;
    /**
     * 状态码
     */
    private Integer httpStatus;

    /**
     * 类名
     */
    private String clazzName;

    /**
     * 方法名
     */
    private String method;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方式
     */
    private String methodType;

    /**
     * 耗时
     */
    private long costTime;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 参数
     */
    private String params;

    /**
     * 返回值
     */
    private String result;

    /**
     * 当前系统版本
     */
    private String version;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 用户token
     */
    private String token;
    /**
     * UA信息
     */
    private String ua;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 平台类型
     */
    private String clientPlatform;
    /**
     * 系统类型
     */
    private String clientOS;
    /**
     * 系统版本
     */
    private String clientOSVersion;
    /**
     * 浏览器
     */
    private String clientBrowser;
    /**
     * 浏览器版本
     */
    private String clientBrowserVersion;
    /**
     * 是否移动设备
     */
    private Boolean clientIsMobile;
    /**
     * 引擎
     */
    private String clientEngine;
    /**
     * 引擎版本
     */
    private String clientEngineVersion;

    /**
     * 进入时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;


    /**
     * 是否成功
     */
    private Boolean isSuccess;

    /**
     * 异常信息
     */
    private String exceptInfo;

    /**
     * 创建时间
     */
    private String crtTime;
}
