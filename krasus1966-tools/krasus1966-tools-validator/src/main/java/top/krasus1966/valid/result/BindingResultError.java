package top.krasus1966.valid.result;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取BindingResult中数据验证错误信息
 *
 * @author Krasus1966
 * @date 2020/10/28 21:54
 **/
public class BindingResultError {

    private BindingResultError() {
    }

    /**
     * 获取所有错误
     *
     * @param result 结果集
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @method getError
     * @author krasus1966
     * @date 2022/4/15 22:21
     * @description 获取所有错误
     */
    public static Map<String, String> getError(BindingResult result) {
        if (!result.hasErrors()) {
            return new HashMap<>();
        }
        return result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,
                FieldError::getDefaultMessage));
    }

    /**
     * 快速失败返回，只返回第一条错误
     *
     * @param result 结果集
     * @return java.lang.String
     * @method getErrorStr
     * @author krasus1966
     * @date 2022/4/15 22:20
     * @description 获取所有错误
     */
    public static FieldError getErrorStr(BindingResult result) {
        if (!result.hasErrors()) {
            return null;
        }
        return result.getFieldErrors().get(0);
    }
}
