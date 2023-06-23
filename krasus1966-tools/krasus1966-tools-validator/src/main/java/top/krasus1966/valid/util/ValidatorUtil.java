package top.krasus1966.valid.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.List;

/**
 * 参数校验工具类，校验的是属性上的校验注解，此方式校验不会区分分组
 *
 * @author Krasus1966
 * @date 2022/11/21 20:38
 **/
public class ValidatorUtil {
    private static final SpringValidatorAdapter validator;

    static {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator jakartaValidator = validatorFactory.getValidator();
            validator = new SpringValidatorAdapter(jakartaValidator);
        }
    }

    private ValidatorUtil() {
    }

    /**
     * 校验data，无分组（带分组的不走）
     *
     * @param data 数据对象
     * @return void
     * @throws BindException
     * @method validate
     * @author krasus1966
     * @date 2022/11/21 21:03
     * @description 校验data，无分组（带分组的不走）
     */
    public static <T> void validate(T data) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(data,
                data.getClass().getSimpleName());
        validator.validate(data, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }

    /**
     * 校验对象，分组
     *
     * @param data  数据对象
     * @param hints 分组对象
     * @return void
     * @throws BindException
     * @method validate
     * @author krasus1966
     * @date 2022/11/21 21:04
     * @description 校验对象，分组
     */
    public static <T> void validate(T data, Object... hints) throws BindException {
        BindingResult bindingResult = new BeanPropertyBindingResult(data,
                data.getClass().getSimpleName());
        validator.validate(data, bindingResult, hints);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
    }

    /**
     * 校验List对象，无分组（带分组的不走）
     *
     * @param data list对象
     * @return void
     * @throws BindException
     * @method validate
     * @author krasus1966
     * @date 2022/11/21 21:04
     * @description 校验List对象，无分组（带分组的不走）
     */
    public static <T> void validate(List<T> data) throws BindException {
        for (int i = 0; i < data.size(); i++) {
            BindingResult bindingResult = new BeanPropertyBindingResult(data.get(i),
                    data.get(i).getClass().getSimpleName());
            validator.validate(data.get(i), bindingResult);
            if (bindingResult.hasErrors()) {
                throw new BindException(bindingResult);
            }
        }
    }

    /**
     * 校验list对象-分组
     *
     * @param data  list对象
     * @param hints 分组对象
     * @return void
     * @throws BindException
     * @method validate
     * @author krasus1966
     * @date 2022/11/21 21:05
     * @description 校验list对象-分组
     */
    public static <T> void validate(List<T> data, Object... hints) throws BindException {
        for (T datum : data) {
            BindingResult bindingResult = new BeanPropertyBindingResult(datum,
                    datum.getClass().getSimpleName());
            validator.validate(datum, bindingResult, hints);
            if (bindingResult.hasErrors()) {
                throw new BindException(bindingResult);
            }
        }
    }
}
