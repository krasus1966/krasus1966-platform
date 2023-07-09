package top.krasus1966.liteflow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.krasus1966.common.rule_engine.entity.RuleScript;
import top.krasus1966.core.db.entity.AbstractPersistent;

/**
 * @author Krasus1966
 * @date 2023/7/8 18:29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("liteflow_script")
public class LiteflowScriptPersistent extends AbstractPersistent {

    @TableField
    private String applicationName;

    @TableField
    private String scriptId;

    @TableField
    private String scriptName;

    @TableField
    private String scriptData;

    @TableField
    private String scriptType;

    @TableField
    private String scriptLanguage;

    public LiteflowScriptPersistent(RuleScript script) {
        super.setId(script.getId());
        this.applicationName = script.getAppName();
        this.scriptId = script.getScriptKey();
        this.scriptName = script.getScriptName();
        this.scriptData = script.getScriptData();
        this.scriptType = script.getScriptType();
        this.scriptLanguage = script.getScriptLanguage();
    }
}
