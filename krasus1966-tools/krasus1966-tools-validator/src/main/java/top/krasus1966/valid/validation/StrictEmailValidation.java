package top.krasus1966.valid.validation;

import top.krasus1966.valid.anno.valid.StrictEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Email;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Krasus1966
 * @date 2022/11/15 20:46
 **/
public class StrictEmailValidation implements ConstraintValidator<StrictEmail, String> {

    private Pattern pattern;

    @Override
    public void initialize(StrictEmail strictEmail) {
        this.pattern = Pattern.compile(strictEmail.regexp());
    }

    @Email
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateEmail(value);
    }

    /**
     * 验证邮箱-严格
     * 使用正则表达式验证邮箱地址，默认正则表达式来源于菜鸟工具
     *
     * @param email 邮箱地址
     * @return boolean
     * @method validateEmail
     * @author krasus1966
     * @date 2022/11/15 20:52
     * @description 验证邮箱-严格
     */
    private boolean validateEmail(final String email) {
        Matcher matcher = this.pattern.matcher(email);
        return matcher.matches();
    }
}
