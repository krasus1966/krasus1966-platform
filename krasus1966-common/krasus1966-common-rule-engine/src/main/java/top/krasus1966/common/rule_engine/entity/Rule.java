package top.krasus1966.common.rule_engine.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Krasus1966
 * @date 2023/7/9 15:38
 **/
@Data
public class Rule {

    private String id;
    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    private String appName;
    /**
     * key，唯一标识
     */
    @NotBlank(message = "ruleKey不能为空")
    private String ruleKey;
    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;
    /**
     * 规则内容
     */
    @NotBlank(message = "规则内容不能为空")
    private String ruleData;
}
