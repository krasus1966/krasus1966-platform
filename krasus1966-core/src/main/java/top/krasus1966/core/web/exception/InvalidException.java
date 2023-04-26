package top.krasus1966.core.web.exception;

import org.springframework.http.HttpStatus;
import top.krasus1966.core.base.exception.CommonException;

/**
 * 非法异常
 *
 * @author Krasus1966
 * @date 2021/6/12 16:15
 **/
public class InvalidException extends CommonException {

    private static final long serialVersionUID = 5876565602768526540L;

    public InvalidException() {
        super(HttpStatus.FORBIDDEN.getReasonPhrase(), HttpStatus.FORBIDDEN.value());
    }

    public InvalidException(String message) {
        super(message, HttpStatus.FORBIDDEN.value());
    }
}
