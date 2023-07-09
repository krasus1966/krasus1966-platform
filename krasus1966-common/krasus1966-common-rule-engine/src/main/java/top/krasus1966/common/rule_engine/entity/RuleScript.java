package top.krasus1966.common.rule_engine.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Krasus1966
 * @date 2023/7/9 15:38
 **/
@Data
public class RuleScript {

    private String id;
    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    private String appName;

    /**
     * 脚本key
     */
    @NotBlank(message = "key不能为空")
    private String scriptKey;

    /**
     * 脚本名称
     */
    @NotBlank(message = "脚本名称不能为空")
    private String scriptName;

    /**
     * 脚本内容
     */
    @NotBlank(message = "脚本内容不能为空")
    private String scriptData;

    /**
     * 脚本类型
     */
    @NotBlank(message = "脚本类型不能为空")
    private String scriptType;

    /**
     * 脚本语言
     */
    @NotBlank(message = "脚本语言不能为空")
    private String scriptLanguage;
}
