package top.krasus1966.core.web.handler;


import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.krasus1966.core.base.enums.ResultsEnum;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.base.exception.CommonException;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.exception.CaptchaException;
import top.krasus1966.core.web.exception.InvalidException;
import top.krasus1966.core.web.exception.NotFoundException;
import top.krasus1966.core.web.exception.UnAuthorizedException;



/**
 * 通用自定义异常处理
 *
 * @author Krasus1966
 * @date 2020/10/28 18:03
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理
     *
     * @param e        自定义异常
     * @param response 响应
     * @return top.krasus1966.base.result.R<java.lang.String>
     * @method errorHandler
     * @author krasus1966
     * @date 2022/1/4 22:03
     * @description 自定义异常处理
     */
    @ExceptionHandler(value = {CommonException.class, BizException.class, NotFoundException.class
            , UnAuthorizedException.class, InvalidException.class})
    public R<String> errorHandler(CommonException e, HttpServletResponse response) {
        String message = e.getMessage();
        Integer code = e.getCode();
        response.setStatus(code);
        return R.parse(code, message, null);
    }

    /**
     * 图形验证码异常处理
     *
     * @param e 图形验证码异常
     * @return top.krasus1966.base_project.common.core.entity.R<java.lang.String>
     * @method errorHandler
     * @author krasus1966
     * @date 2022/11/2 15:20
     * @description 自定义异常处理
     */
    @ExceptionHandler(value = CaptchaException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<String> errorHandler(CaptchaException e) {
        log.error("CaptchaException:code={},msg={}", ResultsEnum.CAPTCHA_ERROR.getCode(),
                e.getMessage());
        return R.parse(ResultsEnum.CAPTCHA_ERROR.getCode(), e.getMessage(), null);
    }

    /**
     * 请求方式异常
     *
     * @param e 请求方式异常
     * @return top.krasus1966.base.result.R
     * @method errorHandler
     * @author krasus1966
     * @date 2022/1/9 05:35
     * @description 请求方式异常
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> errorHandler(HttpRequestMethodNotSupportedException e) {
        return R.failed(ResultsEnum.REQUEST_METHOD_NOT_SUPPORTED);
    }

    /**
     * 资源不存在异常
     *
     * @param e 资源不存在异常
     * @return top.krasus1966.base.result.R
     * @method errorHandler
     * @author krasus1966
     * @date 2022/1/9 05:35
     * @description 资源不存在异常
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R<String> errorHandler(NoHandlerFoundException e) {
        return R.failed(ResultsEnum.PATH_NOT_EXIST);
    }

    /**
     * 资源不存在异常
     *
     * @param e 资源不存在异常
     * @return top.krasus1966.base.result.R
     * @method errorHandler
     * @author krasus1966
     * @date 2022/1/9 05:35
     * @description 资源不存在异常
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> errorHandler(MissingServletRequestParameterException e) {
        return R.failed(ResultsEnum.PARAM_NOT_VALID);
    }

    /**
     * 请求格式异常-针对@RequestBody
     *
     * @param e 资源不存在异常
     * @return top.krasus1966.base.result.R
     * @method errorHandler
     * @author krasus1966
     * @date 2022/1/9 05:35
     * @description 请求格式异常-针对@ResponseBody
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> errorHandler(HttpMessageNotReadableException e) {
        return R.failed(ResultsEnum.PARAM_NOT_FOUND);
    }

    /**
     * 未知异常处理
     *
     * @param e 异常
     * @return top.krasus1966.base_project.common.core.entity.R
     * @method commonErrorHandler
     * @author krasus1966
     * @date 2022/11/2 15:19
     * @description 未知异常处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> commonErrorHandler(Exception e) {
//        log.error("{}:{}", e.getClass().getSimpleName(), e.getMessage());
//        log.error("简略日志:{}:{}",e.getClass().getSimpleName(), e.getLocalizedMessage());
        log.error("服务器内部异常，详细日志:", e);
        return R.failed(ResultsEnum.SERVER_UNEXCEPTION_ERROR);
    }
}
