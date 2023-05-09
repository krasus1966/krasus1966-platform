package top.krasus1966.core.base.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Krasus1966
 * @date 2021/6/10 23:55
 **/
public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 180318802880258762L;
    private final String error;
    private final Integer code;

    public CommonException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        this.error = "";
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CommonException(String message) {
        super(message);
        this.error = "";
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CommonException(String message, String error) {
        super(message);
        this.error = error;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CommonException(String message, Integer code) {
        super(message);
        this.error = "";
        this.code = code;
    }

    public CommonException(String message, String error, Integer code) {
        super(message);
        this.error = error;
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public Integer getCode() {
        return code;
    }
}
