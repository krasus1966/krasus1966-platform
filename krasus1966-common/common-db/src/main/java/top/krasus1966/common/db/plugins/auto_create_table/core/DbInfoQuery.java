package top.krasus1966.common.db.plugins.auto_create_table.core;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import top.krasus1966.common.db.plugins.auto_create_table.entity.ColumnInfo;
import top.krasus1966.common.db.plugins.auto_create_table.entity.TableInfo;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据库meta查询
 *
 * @author krasus1966
 * @date 2024/9/12 18:11
 **/
@Slf4j
public class DbInfoQuery {

    /***
     * 获取数据库中表列表
     * @param databaseMetaData 元数据
     * @param catalog 数据库名称
     * @param schemaPattern 数据库名称
     * @param tableNamePattern 匹配表名称
     * @param types 查询类型
     * @return java.util.List<TableInfo>
     * @method getTables
     * @author krasus1966
     * @date 2024/9/12
     * @description 获取数据库中表列表
     **/
    public static Map<String, TableInfo> getTables(DatabaseMetaData databaseMetaData, String catalog,
                                                   String schemaPattern, String tableNamePattern, String[] types) {
        Map<String, TableInfo> tables = new HashMap<>();
        try (ResultSet resultSet = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types)) {
            TableInfo table;
            while (resultSet.next()) {
                table = new TableInfo();
                table.setName(resultSet.getString("TABLE_NAME"));
                table.setRemarks(formatComment(resultSet.getString("REMARKS")));
                table.setTableType(resultSet.getString("TABLE_TYPE"));
                tables.put(resultSet.getString("TABLE_NAME"), table);
            }
        } catch (SQLException e) {
            throw new RuntimeException("读取数据库表信息出现错误", e);
        }
        return tables;
    }

    /***
     * 查询表列表，key为表名，value为注释，不查询视图
     * @param databaseMetaData
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @throws
     * @method getTablesWithNoView
     * @author krasus1966
     * @date 2024/9/13
     * @description 查询表列表，key为表名，value为注释，不查询视图
     **/
    public static Map<String, String> getTablesWithNoView(DatabaseMetaData databaseMetaData, String catalog,
                                                          String schemaPattern, String tableNamePattern) {
        Map<String, TableInfo> tables = getTables(databaseMetaData, catalog, schemaPattern, tableNamePattern,
                new String[]{"TABLE"});
        Map<String, String> resultTables = new HashMap<>();
        for (Map.Entry<String, TableInfo> table : tables.entrySet()) {
            resultTables.put(table.getKey(), table.getValue().getRemarks());
        }
        return resultTables;
    }

    /***
     * 获取表结构
     * @param databaseMetaData 元数据
     * @param catalog 数据库名称
     * @param schema 数据库名称
     * @param tableName 表名
     * @param primaryKeys 主键列表
     * @return java.util.List<ColumnInfo>
     * @method getColumnsInfo
     * @author krasus1966
     * @date 2024/9/12
     * @description 获取表结构
     **/
    public static List<ColumnInfo> getColumnsInfo(DatabaseMetaData databaseMetaData, String catalog, String schema,
                                                  String tableName, Set<String> primaryKeys) {
        List<ColumnInfo> columnsInfoList = new ArrayList<>();
        try (ResultSet resultSet = databaseMetaData.getColumns(catalog, schema, tableName, "%")) {
            while (resultSet.next()) {
                ColumnInfo column = new ColumnInfo();
                String name = resultSet.getString("COLUMN_NAME");
                column.setName(name);
                column.setPrimaryKey(primaryKeys.contains(name));
                column.setJdbcType(JdbcType.forCode(resultSet.getInt("DATA_TYPE")));
                column.setLength(resultSet.getInt("COLUMN_SIZE"));
                column.setScale(resultSet.getInt("DECIMAL_DIGITS"));
                column.setRemarks(formatComment(resultSet.getString("REMARKS")));
                column.setDefaultValue(resultSet.getString("COLUMN_DEF"));
                column.setNullable(resultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable ? "NULL" : "NOT " +
                        "NULL");
                try {
                    column.setAutoIncrement("YES".equals(resultSet.getString("IS_AUTOINCREMENT")));
                } catch (SQLException sqlException) {
                    log.warn("获取IS_AUTOINCREMENT出现异常:", sqlException);
                }
                columnsInfoList.add(column);
            }
            return columnsInfoList;
        } catch (SQLException e) {
            throw new RuntimeException("读取表字段信息:" + tableName + "错误:", e);
        }
    }

    /***
     * 获取主键信息
     * @param databaseMetaData
     * @param catalog
     * @param schema
     * @param tableName
     * @return java.util.Set<java.lang.String>
     * @method getPrimaryKeys
     * @author krasus1966
     * @date 2024/9/13
     * @description 获取主键信息
     **/
    public static Set<String> getPrimaryKeys(DatabaseMetaData databaseMetaData, String catalog, String schema,
                                             String tableName) {
        Set<String> primaryKeys = new HashSet<>();
        try (ResultSet primaryKeysResultSet = databaseMetaData.getPrimaryKeys(catalog, schema, tableName)) {
            while (primaryKeysResultSet.next()) {
                String columnName = primaryKeysResultSet.getString("COLUMN_NAME");
                primaryKeys.add(columnName);
            }
            if (primaryKeys.size() > 1) {
                log.warn("当前表:{}，存在多主键情况！", tableName);
            }
        } catch (SQLException e) {
            throw new RuntimeException("读取表主键信息:" + tableName + "错误:", e);
        }
        return primaryKeys;
    }

    /***
     * 获取索引信息
     * @param databaseMetaData
     * @param catalog
     * @param schema
     * @param tableName
     * @return java.util.Set<java.lang.String>
     * @method getPrimaryKeys
     * @author krasus1966
     * @date 2024/9/13
     * @description 获取主键信息
     **/
    public static Map<String, String> getIndexKeys(DatabaseMetaData databaseMetaData, String catalog, String schema,
                                                   String tableName) {
        Map<String, String> primaryKeys = new HashMap<>();
        try (ResultSet primaryKeysResultSet = databaseMetaData.getIndexInfo(catalog, schema, tableName, false, false)) {
            while (primaryKeysResultSet.next()) {
                String columnName = primaryKeysResultSet.getString("COLUMN_NAME");
                String indexName = primaryKeysResultSet.getString("INDEX_NAME");
                String NON_UNIQUE = primaryKeysResultSet.getString("NON_UNIQUE");

                // 过滤主键和唯一索引
                if (!"PRIMARY".equals(indexName) && "1".equals(NON_UNIQUE)) {
                    primaryKeys.put(indexName, columnName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("读取表主键信息:" + tableName + "错误:", e);
        }
        return primaryKeys;
    }

    /***
     * 格式化注释
     * @param comment 注释内容
     * @return java.lang.String
     * @method formatComment
     * @author krasus1966
     * @date 2024/9/12
     * @description 格式化注释
     **/
    public static String formatComment(String comment) {
        return StringUtils.isBlank(comment) ? StringPool.EMPTY : comment.replaceAll("\r\n", "\t");
    }
}
