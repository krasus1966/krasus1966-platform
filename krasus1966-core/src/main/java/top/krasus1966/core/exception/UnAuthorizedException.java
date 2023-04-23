package top.krasus1966.core.exception;


import org.springframework.http.HttpStatus;
import top.krasus1966.core.constant.Constants;


/**
 * 无权限异常
 *
 * @author Krasus1966
 * @date 2021/6/10 23:49
 **/
public class UnAuthorizedException extends CommonException {

    private static final long serialVersionUID = 9119933372847796084L;

    public UnAuthorizedException() {
        super(Constants.Exception.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value());
    }

    public UnAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
