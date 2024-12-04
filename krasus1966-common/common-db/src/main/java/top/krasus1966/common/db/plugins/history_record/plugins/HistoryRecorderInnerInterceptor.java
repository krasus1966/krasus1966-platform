package top.krasus1966.common.db.plugins.history_record.plugins;


import com.baomidou.mybatisplus.extension.plugins.inner.DataChangeRecorderInnerInterceptor;
import top.krasus1966.common.core.util.SpringUtil;
import top.krasus1966.common.db.plugins.history_record.po.SysHistoryRecord;
import top.krasus1966.common.db.plugins.history_record.service.ISysHistoryRecordService;

import java.util.HashSet;
import java.util.Set;

/**
 * 数据变动记录插件
 *
 * @author krasus1966
 * @date 2024/11/27 17:41
 **/
public class HistoryRecorderInnerInterceptor extends DataChangeRecorderInnerInterceptor {

    private final Set<String> ignoredTable = new HashSet<>();//全部表的这些字段名，INSERT/UPDATE都忽略，delete暂时保留

    @Override
    protected void dealOperationResult(OperationResult operationResult) {
        if (!ignoredTable.contains(operationResult.getTableName())) {
            SysHistoryRecord record = new SysHistoryRecord();
            record.setTableName(operationResult.getTableName());
            record.setActionType(operationResult.getOperation());
            record.setCostTime(operationResult.getCost());
            record.setChangeJson(operationResult.getChangedData());
            SpringUtil.getBean(ISysHistoryRecordService.class).save(record);
        }
    }

    public void addIgnoredTable(String tableName) {
        ignoredTable.add(tableName);
    }
}
