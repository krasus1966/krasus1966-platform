package top.krasus1966.core.enums;

/**
 * 结果返回Enum
 *
 * @author Krasus1966
 * @date 2020/10/8 21:38
 **/
public enum ResultsEnum implements IResultsEnum {

    /**
     * 返回结果
     */
    SUCCESS(200, "操作成功！"),
    LOG_OUT_SUCCESS(200, "退出登录成功！"),
    REQUEST_METHOD_NOT_SUPPORTED(400, "请求方式不正确"),
    UN_LOGIN(403, "用户未登录！"),
    CAPTCHA_ERROR(403, "验证码不正确！"),
    NO_AUTH_INFO(403, "无权限！"),
    USER_FROZEN(403, "此用户已被冻结！"),
    SESSION_INVALID(403, "会话失效，请重新登录！"),
    UPLOAD_FILE_NOT_EXIST(404, "上传的文件不存在"),
    NO_UPLOAD_FILE(404, "请选择上传的文件!"),
    PATH_NOT_EXIST(404, "请求地址不存在"),
    PARAM_NOT_VALID(412, "参数信息不合法！"),
    SERVER_UNEXCEPTION_ERROR(500, "服务器内部错误，请联系管理员"),
    FAILED(500, "操作失败！"),
    LOG_OUT_FAIL(500, "退出登陆失败！"),
    SMS_CODE_TIME_OUT(500, "验证码已过期"),
    SMS_CODE_ERROR(500, "验证码不匹配"),
    PASSPORT_TOO_BUSY(500, "发送短信太过频繁！"),

    ID_BLANK_ERROR(500, "id不能为空！"),
    ;

    private final Integer code;
    private final String message;

    ResultsEnum(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
