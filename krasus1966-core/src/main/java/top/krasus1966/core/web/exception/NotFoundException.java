package top.krasus1966.core.web.exception;

import org.springframework.http.HttpStatus;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.base.exception.CommonException;


/**
 * 资源不存在异常
 *
 * @author Krasus1966
 * @date 2021/6/10 23:21
 **/
public class NotFoundException extends CommonException {
    private static final long serialVersionUID = -1792482304657464858L;

    public NotFoundException() {
        super(Constants.Exception.NOT_FOUND, HttpStatus.NOT_FOUND.value());
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
