package top.krasus1966.common.db.plugins.auto_create_table.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import top.krasus1966.common.db.plugins.auto_create_table.core.IAutoCreateSQLService;
import top.krasus1966.common.db.plugins.auto_create_table.entity.ColumnInfo;
import top.krasus1966.common.db.plugins.auto_create_table.entity.FieldToJdbcType;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * MySQL建表语句
 *
 * @author krasus1966
 * @date 2024/9/13 17:29
 **/
@Slf4j
public class MySQLAutoCreateSQLServiceImpl implements IAutoCreateSQLService {

    @Override
    public String dbType() {
        return "MySQL";
    }

    /***
     * 创建表语句
     * @param schemaName 数据库名
     * @param tableName 表名
     * @param columnInfoList 字段列表
     * @return java.lang.String
     * @method createTableSql
     * @author krasus1966
     * @date 2024/9/13
     * @description 创建表语句
     **/
    @Override
    public String createTableSql(String schemaName, String tableName, List<ColumnInfo> columnInfoList) {
        String createTableSql = """
                 CREATE TABLE IF NOT EXISTS `%s`.`%s` (%s);
                """;
        createTableSql = String.format(createTableSql, schemaName, tableName, modifierColumnSql(true, columnInfoList,
                false));
        return createTableSql;
    }

    /***
     * 修改表语句
     * @param schemaName 数据库名
     * @param tableName 表名
     * @param columnInfoList 字段列表
     * @return java.lang.String
     * @method alterTableSql
     * @author krasus1966
     * @date 2024/9/13
     * @description 修改表语句
     **/
    @Override
    public String alterTableSql(String schemaName, String tableName, List<ColumnInfo> columnInfoList,
                                boolean hasPrimaryKey) {
        String createTableSql = "ALTER TABLE `%s`.`%s` %s";
        createTableSql = String.format(createTableSql, schemaName, tableName, modifierColumnSql(false, columnInfoList
                , hasPrimaryKey));
        return createTableSql;
    }

    /***
     * 修改字段语句
     * @param isCreateTable 是否是创建表
     * @param columnInfoList 字段列表
     * @return java.lang.String
     * @method modifierColumnSql
     * @author krasus1966
     * @date 2024/9/13
     * @description 修改字段语句
     **/
    @Override
    public String modifierColumnSql(boolean isCreateTable, List<ColumnInfo> columnInfoList, boolean hasPrimaryKey) {
        StringJoiner sqlJoiner = new StringJoiner(",");
        Set<String> primaryKeySet =
                columnInfoList.stream().filter(ColumnInfo::isPrimaryKey).map(ColumnInfo::getName).collect(Collectors.toSet());
        for (ColumnInfo columnInfo : columnInfoList) {
            StringBuilder columnSql = new StringBuilder((isCreateTable ? "%n" : "") + "`%s` %s%s %s %s %s");
            // 字符串
            FieldToJdbcType fieldToJdbcType = FieldToJdbcType.getFieldToJdbcType(columnInfo.getJavaClassName());
            if (null == fieldToJdbcType) {
                log.warn("存在不支持的Java类型：${}", columnInfo.getJavaClassName());
                continue;
            }
            String dbType = fieldToJdbcType.DB_TYPE;
            String length = "";
            String defaultValue = StringUtils.hasText(columnInfo.getDefaultValue()) ?
                    "DEFAULT " + columnInfo.getDefaultValue() : "";
            String comment = StringUtils.hasText(columnInfo.getRemarks()) ? "COMMENT '" + columnInfo.getRemarks() +
                    "'" : "";

            // 数值类型需要小数位
            if ("DECIMAL".equals(dbType)) {
                length = "(" + columnInfo.getLength() + "," + columnInfo.getScale() + ")";
            } else if ("VARCHAR".equals(dbType) && columnInfo.getLength() >= 300) {
                dbType = "LONGTEXT";
            } else if (fieldToJdbcType.NEED_LENGTH) {
                length = "(" + columnInfo.getLength() + ")";
            }
            if (!isCreateTable) {
                // 非创建表时的语句，ADD/MODIFY COLUMN + sql
                columnSql.insert(0, "%n%s COLUMN ".formatted(columnInfo.isAddField() ? "ADD" : "MODIFY"));
            }
            sqlJoiner.add(columnSql.toString().formatted(columnInfo.getName(), dbType, length,
                    columnInfo.getNullable(), defaultValue, comment));
        }
        return addPrimaryKey(isCreateTable, sqlJoiner, primaryKeySet, hasPrimaryKey);
    }

    /***
     * 判断是否需要添加主键
     * @param isCreateTable 是否新建表
     * @param sqlJoiner sql语句
     * @param primaryKeySet 主键列表
     * @return java.lang.StringBuilder
     * @throws
     * @method addPrimaryKey
     * @author krasus1966
     * @date 2024/9/13
     * @description 判断是否需要添加主键
     **/
    @Override
    public String addPrimaryKey(boolean isCreateTable, StringJoiner sqlJoiner, Set<String> primaryKeySet,
                                boolean hasPrimaryKey) {
        // 是否设置主键
        StringBuilder resultSql = new StringBuilder(sqlJoiner.toString());
        if (!primaryKeySet.isEmpty()) {
            if (!isCreateTable) {
                if (hasPrimaryKey) {
                    resultSql.append(",DROP PRIMARY KEY,ADD ");
                } else {
                    resultSql.append(",ADD ");
                }
            } else {
                resultSql.append(",");
            }
            StringJoiner primaryKeyJoiner = new StringJoiner(",");
            for (String primaryKey : primaryKeySet) {
                primaryKeyJoiner.add("`" + primaryKey + "`");
            }
            resultSql.append("PRIMARY KEY (").append(primaryKeyJoiner).append(")");
        }
        return resultSql.toString();
    }
}
