package top.krasus1966.core.web.exception;

/**
 * @author Krasus1966
 * @date 2021/5/9 00:08
 **/
public class CaptchaException extends RuntimeException {
    public CaptchaException(String msg) {
        super(msg);
    }
}
