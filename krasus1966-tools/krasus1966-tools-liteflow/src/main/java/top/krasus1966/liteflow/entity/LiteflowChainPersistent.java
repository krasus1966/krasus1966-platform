package top.krasus1966.liteflow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.krasus1966.common.rule_engine.entity.Rule;
import top.krasus1966.core.db.entity.AbstractPersistent;

/**
 * @author Krasus1966
 * @date 2023/7/8 18:29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("liteflow_chain")
public class LiteflowChainPersistent extends AbstractPersistent {

    @TableField
    private String applicationName;

    @TableField
    private String chainKey;

    @TableField
    private String chainName;

    @TableField
    private String elData;

    public LiteflowChainPersistent(Rule rule) {
        super.setId(rule.getId());
        this.applicationName = rule.getAppName();
        this.chainKey = rule.getRuleKey();
        this.chainName = rule.getRuleName();
        this.elData = rule.getRuleData();
    }
}
