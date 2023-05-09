package top.krasus1966.common.preview.exception;

import top.krasus1966.core.base.exception.CommonException;

/**
 * @author Krasus1966
 * @date 2023/5/6 23:10
 **/
public class OfficeNotFoundException extends CommonException {
    public OfficeNotFoundException() {
        super("office服务未找到，无法使用office转换服务！");
    }
}
