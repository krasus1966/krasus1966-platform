package top.krasus1966.core.exception;

/**
 * @author Krasus1966
 * @date 2022/10/30 23:15
 **/
public class JsonCastException extends RuntimeException {

    public JsonCastException(String message) {
        super(message);
    }

    public JsonCastException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonCastException(Throwable cause) {
        super(cause);
    }

    public JsonCastException(String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
