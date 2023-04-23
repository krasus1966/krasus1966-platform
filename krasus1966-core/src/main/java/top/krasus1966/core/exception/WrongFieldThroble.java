package top.krasus1966.core.exception;

/**
 * @author Krasus1966
 * @date 2022/10/31 17:51
 **/
public class WrongFieldThroble extends Throwable {
    public WrongFieldThroble() {
    }

    public WrongFieldThroble(String message) {
        super(message);
    }

    public WrongFieldThroble(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongFieldThroble(Throwable cause) {
        super(cause);
    }

    public WrongFieldThroble(String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
