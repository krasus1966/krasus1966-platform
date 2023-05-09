package top.krasus1966.core.crypto.sign.exception;

/**
 * @author Krasus1966
 * @date 2022/10/31 23:46
 **/
public class NotFoundCryptoException extends RuntimeException {

    public NotFoundCryptoException() {
    }

    public NotFoundCryptoException(String message) {
        super(message);
    }

    public NotFoundCryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCryptoException(Throwable cause) {
        super(cause);
    }

    public NotFoundCryptoException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
