package top.krasus1966.core.base.exception;


import top.krasus1966.core.base.enums.IResultsEnum;
import top.krasus1966.core.base.enums.ResultsEnum;

/**
 * 通用业务异常
 *
 * @author Krasus1966
 * @date 2020/10/28 17:33
 **/
public class BizException extends CommonException {

    private static final long serialVersionUID = -221550750202401786L;

    public BizException(IResultsEnum resultEnum) {
        super(resultEnum.getMessage(), resultEnum.getCode());
    }

    public BizException(String msg) {
        super(msg, ResultsEnum.PARAM_NOT_VALID.getCode());
    }

    public BizException(String msg, String error) {
        super(msg, error, ResultsEnum.PARAM_NOT_VALID.getCode());
    }

    public BizException(Integer code, String msg) {
        super(msg, code);
    }

    public BizException(Integer code, String msg, String error) {
        super(msg, error, code);
    }
}
