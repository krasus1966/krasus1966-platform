package top.krasus1966.core.web.entity;


import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.base.enums.IResultsEnum;
import top.krasus1966.core.base.exception.BizException;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用返回封装
 *
 * @author Krasus1966
 * @date 2020/10/8 21:42
 **/
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 9140215144638597826L;

    private Integer code;
    private String msg;
    private T data;

    private LocalDateTime timestamp = LocalDateTime.now();


    private R() {
    }

    public R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功
     *
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method success
     * @author krasus1966
     * @date 2022/10/31 11:14
     * @description 成功
     */
    public static <T> R<T> success() {
        return parse(HttpStatus.OK.value(), Constants.Result.ACTION_OK, null);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method success
     * @author krasus1966
     * @date 2022/10/31 11:00
     * @description 成功
     */
    public static <T> R<T> success(T data) {
        return parse(HttpStatus.OK.value(), Constants.Result.ACTION_OK, data);
    }

    /**
     * 成功
     *
     * @param msg  消息
     * @param data 数据
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method success
     * @author krasus1966
     * @date 2022/10/31 11:01
     * @description 成功
     */
    public static <T> R<T> success(String msg, T data) {
        return parse(HttpStatus.OK.value(), msg, data);
    }

    /**
     * 成功
     *
     * @param resultsEnum 消息
     * @param data        数据
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method success
     * @author krasus1966
     * @date 2022/10/31 11:01
     * @description 成功
     */
    public static <T> R<T> success(IResultsEnum resultsEnum, T data) {
        return parse(resultsEnum.getCode(), resultsEnum.getMessage(), data);
    }

    /**
     * 失败
     *
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method failed
     * @author krasus1966
     * @date 2022/10/31 11:01
     * @description 失败
     */
    public static <T> R<T> failed() {
        return parse(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.Result.ACTION_FAILED,
                null);
    }

    /**
     * 失败
     *
     * @param msg 消息
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method failed
     * @author krasus1966
     * @date 2022/10/31 11:01
     * @description 失败
     */
    public static <T> R<T> failed(String msg) {
        return parse(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    /**
     * 失败
     *
     * @param resultsEnum 消息
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method failed
     * @author krasus1966
     * @date 2022/10/31 11:01
     * @description 失败
     */
    public static <T> R<T> failed(IResultsEnum resultsEnum) {
        return parse(resultsEnum.getCode(), resultsEnum.getMessage(), null);
    }

    /**
     * 通用返回
     *
     * @param code code
     * @param msg  消息
     * @param data 数据对象
     * @return top.krasus1966.base_project.common.core.entity.R<T>
     * @method parse
     * @author krasus1966
     * @date 2022/10/31 10:57
     * @description 通用返回
     */
    public static <T> R<T> parse(Integer code, String msg, T data) {
        return new R<>(code, msg, data);
    }

    /**
     * 异常返回
     *
     * @param error 封装返回信息
     */
    public static void error(IResultsEnum error) {
        throw new BizException(error);
    }

    /**
     * 异常返回
     *
     * @param msg 返回业务异常信息
     */
    public static void error(String msg) {
        throw new BizException(msg);
    }

    /**
     * 异常返回
     *
     * @param code 错误码
     * @param msg  错误内容
     */
    public static void error(Integer code, String msg) {
        throw new BizException(code, msg);
    }

    /**
     * 未登录返回
     *
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static <T> R<T> noLogin() {
        return R.parse(HttpStatus.UNAUTHORIZED.value(), "未登录", null);
    }

    /**
     * 其他客户端登录返回
     *
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static <T> R<T> repeatLogin() {
        return R.parse(HttpStatus.UNAUTHORIZED.value(), "当前用户已在其他客户端登录", null);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static <T> R<T> signErr() {
        return R.parse(HttpStatus.UNAUTHORIZED.value(), "参数签名无效！", null);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static <T> R<T> signRepeat() {
        return R.parse(HttpStatus.UNAUTHORIZED.value(), "禁止重复请求接口！", null);
    }

    /**
     * 无权限返回
     *
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static <T> R<T> noPermission() {
        return R.parse(HttpStatus.UNAUTHORIZED.value(), "无权限", null);
    }

    /**
     * 非法请求返回
     *
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static R illegal() {
        return R.parse(HttpStatus.NOT_ACCEPTABLE.value(), "非法操作", null);
    }

    public boolean isSuccessful() {
        return this.code == HttpStatus.OK.value();
    }
}
