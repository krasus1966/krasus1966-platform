package top.krasus1966.valid.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 参数校验配置
 *
 * @author Krasus1966
 * @date 2021/9/26 00:18
 **/
public class ValidatorConfiguration {

    /**
     * 注入validator
     *
     * @return javax.validation.Validator
     * @method validator
     * @author krasus1966
     * @date 2022/4/18 15:33
     * @description 注入validator
     */
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory =
                Validation.byProvider(HibernateValidator.class)
                        .configure()
                        //快速失败返回模式
                        .addProperty("hibernate.validator.fail_fast", "true")
                        .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    /**
     * 开启快速返回
     * 如果参数校验有异常，直接抛异常，不会进入到 controller，使用全局异常拦截进行拦截
     *
     * @param validator validator bean
     * @return org.springframework.validation.beanvalidation.MethodValidationPostProcessor
     * @method methodValidationPostProcessor
     * @author krasus1966
     * @date 2022/4/18 15:33
     * @description 开启快速返回，如果参数校验有异常，直接抛异常，不会进入到 controller，使用全局异常拦截进行拦截
     */
    @Bean
    @ConditionalOnBean(value = Validator.class)
    public MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
        MethodValidationPostProcessor postProcessor =
                new MethodValidationPostProcessor();
        postProcessor.setValidator(validator);
        return postProcessor;
    }
}
