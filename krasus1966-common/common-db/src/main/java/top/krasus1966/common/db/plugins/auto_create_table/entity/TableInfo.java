package top.krasus1966.common.db.plugins.auto_create_table.entity;

import lombok.Data;

/**
 * 表信息
 *
 * @author krasus1966
 * @date 2024/9/12 18:13
 **/
@Data
public class TableInfo {
    private String name;

    private String remarks;

    private String tableType;
}
