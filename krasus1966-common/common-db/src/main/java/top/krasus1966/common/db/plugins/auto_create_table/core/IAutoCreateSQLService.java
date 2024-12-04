package top.krasus1966.common.db.plugins.auto_create_table.core;


import top.krasus1966.common.db.plugins.auto_create_table.entity.ColumnInfo;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

/**
 * 数据库建立相关SQL
 *
 * @author krasus1966
 * @date 2024/9/13 17:27
 **/
public interface IAutoCreateSQLService {

    /***
     * 数据库类型
     *
     * @return java.lang.String
     * @method dbType
     * @author krasus1966
     * @date 2024/9/13
     * @description 数据库类型
     **/
    String dbType();

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
    String createTableSql(String schemaName, String tableName, List<ColumnInfo> columnInfoList);

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
    String alterTableSql(String schemaName, String tableName, List<ColumnInfo> columnInfoList, boolean hasPrimaryKey);

    /***
     * 修改字段语句
     * @param isAdd 是否是创建表
     * @param columnInfoList 字段列表
     * @return java.lang.String
     * @method modifierColumnSql
     * @author krasus1966
     * @date 2024/9/13
     * @description 修改字段语句
     **/
    String modifierColumnSql(boolean isAdd, List<ColumnInfo> columnInfoList, boolean hasPrimaryKey);

    /***
     * 添加主键语句
     * @param isCreateTable 是否是创建表
     * @param sqlJoiner 前置SQL语句
     * @param primaryKeySet 主键列表
     * @param hasPrimaryKey 历史是否有主键
     * @return java.lang.String
     * @method addPrimaryKey
     * @author krasus1966
     * @date 2024/9/13
     * @description 添加主键语句
     **/
    String addPrimaryKey(boolean isCreateTable, StringJoiner sqlJoiner, Set<String> primaryKeySet,
                         boolean hasPrimaryKey);
}
