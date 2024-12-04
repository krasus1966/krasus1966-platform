package top.krasus1966.common.db.plugins.history_record.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.krasus1966.common.db.entity.AbstractPersistent;
import top.krasus1966.common.db.plugins.auto_create_table.annonation.AutoTableField;
import top.krasus1966.common.db.plugins.auto_create_table.annonation.AutoTableName;

/**
 * 数据变动记录表
 *
 * @author krasus1966
 * @date 2024/11/27 17:20
 **/
@Data
@AutoTableName("sys_history_record")
@TableName("sys_history_record")
public class SysHistoryRecord extends AbstractPersistent {

    @AutoTableField(comment = "表名")
    private String tableName;

    @AutoTableField(comment = "操作方式")
    private String actionType;

    @AutoTableField(comment = "耗时")
    private Long costTime;

    @AutoTableField(comment = "变动内容JSON",length = 2000)
    private String changeJson;
}
