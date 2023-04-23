package top.krasus1966.valid.anno.valid;

import top.krasus1966.valid.validation.PasswordValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Krasus1966
 * @date 2022/11/15 20:25
 **/
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidation.class)
@Documented
public @interface Password {

    /**
     * 默认提示信息 其他提示信息为空的情况下提示此信息
     */
    String message() default "Invalid Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 密码判空失败 提示消息
     */
    String nullMessage() default "";

    /**
     * 最短位数
     */
    int min() default 8;

    /**
     * 最短位数 提示消息
     */
    String minMessage() default "";

    /**
     * 最长位数
     */
    int max() default 30;

    /**
     * 最长位数 提示消息
     */
    String maxMessage() default "";

    /**
     * 包含大写字母数量 0则不生效
     */
    int containUpperCase() default 1;

    /**
     * 包含大写字母数量 提示消息
     */
    String upperCaseMessage() default "";

    /**
     * 包含小写字母数量 0则不生效
     */
    int containLowerCase() default 1;

    /**
     * 包含小写字母数量 提示消息
     */
    String lowerCaseMessage() default "";

    /**
     * 包含特殊字符数量 0则不生效
     */
    int containSpecial() default 1;

    /**
     * 包含特殊字符数量 提示消息
     */
    String specialMessage() default "";

    /**
     * 同字符连续出现数量，达到此数量后密码验证不通过
     */
    int sequentSameCharNum() default 5;

    /**
     * 同字符连续出现 提示消息
     */
    String sameCharMessage() default "";

    /**
     * 允许字母表连续字符
     */
    boolean allowSequentChar() default false;

    /**
     * 字母表连续字符数量，达到此数量后密码验证不通过
     */
    int sequentCharNum() default 5;

    /**
     * 字母表连续字符出现数量 提示消息
     */
    String sequentCharMessage() default "";

    /**
     * 允许连续数字
     */
    boolean allowSequentNumber() default false;

    /**
     * 连续数字数量，达到此数量后密码验证不通过
     */
    int sequentNumberNum() default 5;

    /**
     * 连续数字数量 提示消息
     */
    String sequentNumberMessage() default "";

    /**
     * 允许键盘字符顺序输入
     */
    boolean allowSequentKeyboard() default false;

    /**
     * 连续键盘字符顺序输入数量，达到此数量后密码验证不通过
     */
    int sequentKeyboardNum() default 5;

    /**
     * 连续键盘字符顺序输入数量 提示消息
     */
    String sequentKeyboardMessage() default "";
}
