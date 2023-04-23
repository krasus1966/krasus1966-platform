package top.krasus1966.valid.validation;

import org.passay.*;
import top.krasus1966.valid.anno.valid.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2022/11/15 20:32
 **/
public class PasswordValidation implements ConstraintValidator<Password, String> {

    private int min;
    private int max;
    private int containUpperCase;
    private int containLowerCase;
    private int containSpecial;
    private int sequentSameCharNum;

    private boolean allowSequentChar;
    private int sequentCharNum;
    private boolean allowSequentNumber;
    private int sequentNumberNum;
    private boolean allowSequentKeyboard;
    private int sequentKeyboardNum;

    /**
     * 以下为消息，不存在则走默认消息
     */
    private String defaultMessage;
    private String nullMessage;
    private String minMessage;
    private String maxMessage;
    private String upperCaseMessage;
    private String lowerCaseMessage;
    private String specialMessage;
    private String sameCharMessage;
    private String sequentCharMessage;
    private String sequentNumberMessage;
    private String sequentKeyboardMessage;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || "".equals(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("".equals(nullMessage) ? "Password must " +
                            "not null" : nullMessage)
                    .addConstraintViolation();
            return false;
        }
        PasswordValidator validator = new PasswordValidator(initRuleList());
        RuleResult result = validator.validate(new PasswordData(value));
        parseErrorMessage(result, context);
        return result.isValid();
    }

    /**
     * 初始化密码验证规则
     *
     * @return java.util.List<org.passay.Rule>
     * @method initRuleList
     * @author krasus1966
     * @date 2022/11/15 22:19
     * @description 初始化密码验证规则
     */
    private List<Rule> initRuleList() {
        List<Rule> ruleList = new ArrayList<>();
        ruleList.add(new WhitespaceRule());
        ruleList.add(new LengthRule(min, max));
        if (containUpperCase > 0) {
            ruleList.add(new CharacterRule(EnglishCharacterData.UpperCase, containUpperCase));
        }
        if (containLowerCase > 0) {
            ruleList.add(new CharacterRule(EnglishCharacterData.LowerCase, containLowerCase));
        }
        if (containSpecial > 0) {
            ruleList.add(new CharacterRule(EnglishCharacterData.Special, containSpecial));
        }
        if (sequentSameCharNum > 0) {
            ruleList.add(new RepeatCharactersRule(sequentSameCharNum));
        }
        if (!allowSequentChar) {
            ruleList.add(new IllegalSequenceRule(EnglishSequenceData.Alphabetical,
                    sequentCharNum, false));
        }
        if (!allowSequentNumber) {
            ruleList.add(new IllegalSequenceRule(EnglishSequenceData.Numerical, sequentNumberNum,
                    false));
        }
        if (!allowSequentKeyboard) {
            ruleList.add(new IllegalSequenceRule(EnglishSequenceData.USQwerty, sequentKeyboardNum
                    , false));
        }
        return ruleList;
    }

    /**
     * 存在失败校验，根据校验类型返回新的提示信息
     *
     * @param result  校验结果
     * @param context 校验上下文
     * @return void
     * @method parseErrorMessage
     * @author krasus1966
     * @date 2022/11/15 23:25
     * @description 存在失败校验，根据校验类型返回新的提示信息
     */
    private void parseErrorMessage(RuleResult result, ConstraintValidatorContext context) {
        List<RuleResultDetail> ruleResultDetailList = result.getDetails();
        if (ruleResultDetailList.isEmpty()) {
            return;
        }
        RuleResultDetail ruleResultDetail = ruleResultDetailList.get(0);
        Map<String, Object> paramters = ruleResultDetail.getParameters();
        String message;
        switch (ruleResultDetail.getErrorCode()) {
            case "ILLEGAL_WHITESPACE":
                message = "".equals(this.nullMessage) ? "Password contains a whitespace character."
                        : this.nullMessage;
                break;
            case "TOO_SHORT":
                message = "".equals(this.minMessage) ? String.format("Password must be %1$s or " +
                        "more characters in length.", this.min) : this.minMessage;
                break;
            case "TOO_LONG":
                message = "".equals(this.minMessage) ? String.format("Password must be no more " +
                        "than %1$s characters in length.", this.max) : this.maxMessage;
                break;
            case "ILLEGAL_REPEATED_CHARS":
                message = "".equals(this.sameCharMessage) ? String.format("Password contains more" +
                                " repeated characters, same characters can only be repeated %1$s " +
                                "times.",
                        this.sequentSameCharNum) : this.sameCharMessage;
                break;
            case "INSUFFICIENT_UPPERCASE":
                message = "".equals(this.upperCaseMessage) ? String.format("Password must contain" +
                        " %1$s " +
                        "or more uppercase characters.", this.containUpperCase) :
                        this.upperCaseMessage;
                break;
            case "INSUFFICIENT_LOWERCASE":
                message = "".equals(this.lowerCaseMessage) ? String.format("Password must contain" +
                        " %1$s " +
                        "or more lowercase characters.", this.containLowerCase) :
                        this.lowerCaseMessage;
                break;
            case "INSUFFICIENT_SPECIAL":
                message = "".equals(this.specialMessage) ? String.format("Password must contain " +
                        "%1$s " +
                        "or more special characters.", this.containSpecial) : this.specialMessage;
                break;
            case "ILLEGAL_ALPHABETICAL_SEQUENCE":
                message = "".equals(this.sequentCharMessage) ? String.format("Password " +
                        "contains the " +
                        "illegal alphabetical sequence '%1$s'.", paramters.get("sequence")) :
                        this.sequentCharMessage;
                break;
            case "ILLEGAL_NUMERICAL_SEQUENCE":
                message = "".equals(this.sequentNumberMessage) ? String.format("Password contains" +
                        " the " +
                        "illegal numerical sequence '%1$s'.", paramters.get("sequence")) :
                        this.sequentNumberMessage;
                break;
            case "ILLEGAL_QWERTY_SEQUENCE":
                message = "".equals(this.sequentKeyboardMessage) ? String.format("Password must " +
                        "be no more " +
                        "than %1$s characters in length.", paramters.get("sequence")) :
                        this.sequentKeyboardMessage;
                break;
            default:
                message = this.defaultMessage;
                break;
        }
        // 禁用默认的返回消息
        context.disableDefaultConstraintViolation();
        // 设置返回的消息
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    @Size
    @Override
    public void initialize(Password password) {
        this.min = password.min();
        this.max = password.max();
        this.containUpperCase = password.containUpperCase();
        this.containLowerCase = password.containLowerCase();
        this.containSpecial = password.containSpecial();
        this.sequentSameCharNum = password.sequentSameCharNum();
        this.allowSequentChar = password.allowSequentChar();
        this.sequentCharNum = password.sequentCharNum();
        this.allowSequentNumber = password.allowSequentNumber();
        this.sequentNumberNum = password.sequentNumberNum();
        this.allowSequentKeyboard = password.allowSequentKeyboard();
        this.sequentKeyboardNum = password.sequentKeyboardNum();

        // 以下为提示消息
        this.defaultMessage = password.message();
        this.nullMessage = password.nullMessage();
        this.minMessage = password.minMessage();
        this.maxMessage = password.maxMessage();
        this.upperCaseMessage = password.upperCaseMessage();
        this.lowerCaseMessage = password.lowerCaseMessage();
        this.sameCharMessage = password.sameCharMessage();
        this.specialMessage = password.specialMessage();
        this.sequentCharMessage = password.sequentCharMessage();
        this.sequentNumberMessage = password.sequentNumberMessage();
        this.sequentKeyboardMessage = password.sequentKeyboardMessage();
    }
}
