package top.krasus1966.liteflow.entity.persistent;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.krasus1966.core.db.entity.AbstractPersistent;

/**
 * @author Krasus1966
 * @date 2023/7/8 18:29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
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
}
