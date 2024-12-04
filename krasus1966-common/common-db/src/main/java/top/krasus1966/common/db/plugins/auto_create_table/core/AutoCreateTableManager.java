package top.krasus1966.common.db.plugins.auto_create_table.core;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.krasus1966.common.db.plugins.auto_create_table.annonation.AutoTableField;
import top.krasus1966.common.db.plugins.auto_create_table.annonation.AutoTableName;
import top.krasus1966.common.db.plugins.auto_create_table.entity.ColumnInfo;
import top.krasus1966.common.db.plugins.auto_create_table.entity.FieldToJdbcType;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自动建表工具
 *
 * @author krasus1966
 * @date 2024/9/12 16:06
 **/
@Slf4j
public class AutoCreateTableManager {

    private final ConfigurableListableBeanFactory configurableListableBeanFactory;
    private final SqlSessionFactory sqlSessionFactory;
    private final String packageName;

    public AutoCreateTableManager(ConfigurableListableBeanFactory configurableListableBeanFactory,
                                  SqlSessionFactory sqlSessionFactory, String packageName) {
        this.configurableListableBeanFactory = configurableListableBeanFactory;
        this.sqlSessionFactory = sqlSessionFactory;
        this.packageName = packageName;
    }

    @PostConstruct
    public void init() throws SQLException {
        this.autoCreateTable();
    }

    /***
     * 自动扫描并创建表结构
     * @return void
     * @method autoCreateTable
     * @author krasus1966
     * @date 2024/9/12
     * @description 自动扫描并创建表结构
     **/
    public void autoCreateTable() {
        log.info("开始同步数据库表结构！");
        long beginTime = System.currentTimeMillis();
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Connection connection = sqlSession.getConnection();
            String catalog = connection.getCatalog();
            String schema = connection.getSchema();
            String dbName = StringUtils.hasText(catalog) ? catalog : schema;

            if (!StringUtils.hasText(dbName)) {
                log.warn("无法获取数据库名称！");
                return;
            }
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            if (!StringUtils.hasText(databaseProductName)) {
                log.warn("无法获取数据库类型！");
                return;
            }
            IAutoCreateSQLService autoCreateSQLService = AutoCreateSQLServiceFactory.getService(databaseProductName);
            if (null == autoCreateSQLService) {
                log.error("暂不支持数据库`{}`的表结构自动创建！", databaseProductName);
                return;
            }
            // 当前代码中的表结构
            List<Class<?>> tableList = this.scanAutoTableList(packageName);
            if (tableList.isEmpty()) {
                log.warn("未获取到代码中配置的数据库结构！");
                return;
            }
            List<ColumnInfo> columnInfoList = this.scanAutoFieldList(tableList);
            if (columnInfoList.isEmpty()) {
                log.warn("未获取到代码中配置的数据库表结构！");
                return;
            }

            // 查询数据库中的表结构
            Map<String, String> tables = DbInfoQuery.getTablesWithNoView(metaData, catalog, schema, null);

            // 开始对比表结构
            Map<String, List<ColumnInfo>> groupByJavaTableColumnMap =
                    columnInfoList.stream().collect(Collectors.groupingBy(ColumnInfo::getTable));

            // 循环Java类表结构
            for (Map.Entry<String, List<ColumnInfo>> tableEntry : groupByJavaTableColumnMap.entrySet()) {
                if (!tables.containsKey(tableEntry.getKey())) {
                    // 创建表
                    String tableSql = autoCreateSQLService.createTableSql(dbName, tableEntry.getKey(),
                            tableEntry.getValue());
                    log.info("执行创建表结构语句：{}", tableSql);
                    boolean success = executeSql(connection, tableSql);
                    if (!success) {
                        log.error("创建`{}`表结构失败！", tableEntry.getKey());
                    }
                } else {
                    // 查询数据库主键列表
                    Set<String> primaryKeys = DbInfoQuery.getPrimaryKeys(metaData, catalog, schema,
                            tableEntry.getKey());
                    // Map<String,String> indexKeys = DbInfoQuery.getIndexKeys(metaData, catalog, schema, tableEntry
                    // .getKey());
                    // 对比字段信息是否一致
                    List<ColumnInfo> needUpdateColumn = scanNeedChangeColumns(DbInfoQuery.getColumnsInfo(metaData,
                                    catalog, schema, tableEntry.getKey(), primaryKeys),
                            groupByJavaTableColumnMap.get(tableEntry.getKey()));
                    ColumnInfo javaColumnInfo = groupByJavaTableColumnMap.get(tableEntry.getKey()).get(0);
                    String note = tables.get(tableEntry.getKey());

                    if (!needUpdateColumn.isEmpty() || (StringUtils.hasText(javaColumnInfo.getNotes()) && !javaColumnInfo.getNotes().equals(note))) {
                        String tableSql = autoCreateSQLService.alterTableSql(dbName, tableEntry.getKey(),
                                needUpdateColumn, !primaryKeys.isEmpty());
                        if (StringUtils.hasText(javaColumnInfo.getNotes()) && !javaColumnInfo.getNotes().equals(note)) {
                            if (!needUpdateColumn.isEmpty()) {
                                tableSql += ",";
                            }
                            tableSql += " COMMENT='" + javaColumnInfo.getNotes() + "'";
                        }
                        log.info("执行修改表结构语句：\n{}", tableSql);
                        boolean success = executeSql(connection, tableSql);
                        if (!success) {
                            log.error("修改`{}`表结构失败！", tableEntry.getKey());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("数据库表结构同步失败！", e);
        }
        log.info("数据库表结构同步完成！耗时：{}秒",
                BigDecimal.valueOf(System.currentTimeMillis() - beginTime).divide(BigDecimal.valueOf(1000), 2,
                        RoundingMode.HALF_UP));
    }

    /***
     * 执行SQL语句
     * @param connection
     * @param sql
     * @return boolean
     * @method executeSql
     * @author krasus1966
     * @date 2024/9/13
     * @description 执行SQL语句
     **/
    private boolean executeSql(Connection connection, String sql) {
        boolean execute = false;
        try {
            connection.prepareStatement(sql).execute();
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            execute = true;
        } catch (SQLException e) {
            log.error("执行SQL失败！SQL={};", sql, e);
        }
        return execute;
    }

    /***
     * 扫描表
     * @param
     * @return java.util.List<java.lang.Class < ?>>
     * @throws
     * @method scanAutoTableList
     * @author krasus1966
     * @date 2024/9/12
     * @description 扫描表
     **/
    public List<Class<?>> scanAutoTableList(String packageName) {
        List<Class<?>> tableList = new ArrayList<>();
        Map<String, Object> map = configurableListableBeanFactory.getBeansWithAnnotation(AutoTableName.class);
        for (String key : map.keySet()) {
            Class<?> cls = map.get(key).getClass();
            if (cls.getPackageName().startsWith(packageName + ".")) {
                tableList.add(cls);
            }
        }
        return tableList;
    }

    /***
     * 扫描字段
     * @param tableList
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.String>>
     * @throws
     * @method scanAutoFieldList
     * @author krasus1966
     * @date 2024/9/12
     * @description 扫描字段
     **/
    public List<ColumnInfo> scanAutoFieldList(List<Class<?>> tableList) {
        List<ColumnInfo> tableFields = new ArrayList<ColumnInfo>();
        for (Class<?> cls : tableList) {
            if (cls.isAnnotationPresent(AutoTableName.class)) {
                AutoTableName table = cls.getAnnotation(AutoTableName.class);
                List<Field> fields = new ArrayList<>();
                fields.addAll(Arrays.asList(cls.getDeclaredFields()));
                fields.addAll(Arrays.asList(cls.getSuperclass().getDeclaredFields()));
                for (Field field : fields) {
                    if (field.isAnnotationPresent(AutoTableField.class)) {// 标了字段注解
                        ColumnInfo info = new ColumnInfo();
                        AutoTableField col = field.getAnnotation(AutoTableField.class);
                        info.setTable(table.value());
                        info.setPrimaryKey(col.isPrimaryKey());
                        info.setNotes(table.comment());
                        info.setName(StringUtils.hasText(col.value()) ?
                                com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(col.value()) :
                                com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(field.getName()));
                        info.setJavaClassName(field.getType().getName());
                        info.setLength(col.length());
                        info.setScale(col.scale());
                        info.setRemarks(col.comment());
                        info.setDefaultValue("NULL".equals(col.defaultValue()) ? null : col.defaultValue());
                        info.setNullable(col.nullable() && !col.isPrimaryKey() ? "NULL" : "NOT NULL");
                        tableFields.add(info);
                    }
                }
            }
        }
        return tableFields;
    }

    /***
     * 遍历并对比字段信息，返回字段信息不一致的字段信息
     * @param columnsInfo
     * @param javaColumnsInfo
     * @return java.util.List<com.ttsx.common.mybatis.auto_create_table.entity.ColumnInfo>
     * @throws
     * @method scanNeedChangeColumns
     * @author krasus1966
     * @date 2024/9/13
     * @description 遍历并对比字段信息，返回字段信息不一致的字段信息
     **/
    private List<ColumnInfo> scanNeedChangeColumns(List<ColumnInfo> columnsInfo, List<ColumnInfo> javaColumnsInfo) {
        Set<String> dbColumnNameSet = columnsInfo.stream().map(ColumnInfo::getName).collect(Collectors.toSet());
        return javaColumnsInfo.stream().filter(javaColumn -> columnsInfo.stream().noneMatch(item -> compareWithJavaAndDb(javaColumn, item))).peek(item -> item.setAddField(!dbColumnNameSet.contains(item.getName()))).toList();
    }

    /***
     * 比较Java字段和数据库字段是否一致
     * @param javaColumn
     * @param dbColumn
     * @return boolean
     * @throws
     * @method compareWithJavaAndDb
     * @author krasus1966
     * @date 2024/9/13
     * @description 比较Java字段和数据库字段是否一致
     **/
    private boolean compareWithJavaAndDb(ColumnInfo javaColumn, ColumnInfo dbColumn) {
        boolean isSame;
        FieldToJdbcType fieldToJdbcType = FieldToJdbcType.getFieldToJdbcType(javaColumn.getJavaClassName());
        if (null == fieldToJdbcType) {
            return true;
        }
        boolean jdbcTypeIsSame =
                Arrays.stream(fieldToJdbcType.JDBC_TYPE).anyMatch(jdbcType -> jdbcType.TYPE_CODE == dbColumn.getJdbcType().TYPE_CODE);
        boolean lengthIsSame = true;
        boolean scaleIsSame = true;
        // 定长字段过滤，长度只限制字符串
        if (!dbColumn.getJdbcType().equals(JdbcType.LONGVARCHAR) && fieldToJdbcType.NEED_LENGTH) {
            lengthIsSame = javaColumn.getLength() == dbColumn.getLength();

            // 带小数点的，判断小数点变化
            if ("DECIMAL".equals(fieldToJdbcType.DB_TYPE)) {
                scaleIsSame = javaColumn.getScale() == dbColumn.getScale();
            }
        }
        isSame = javaColumn.isPrimaryKey() == dbColumn.isPrimaryKey()
                && javaColumn.getName().equals(dbColumn.getName())
                && javaColumn.getRemarks().equals(dbColumn.getRemarks())
                && lengthIsSame

                && javaColumn.getNullable().equals(dbColumn.getNullable())
                && ObjectUtils.nullSafeEquals(javaColumn.getDefaultValue(), dbColumn.getDefaultValue())
                && jdbcTypeIsSame
                && scaleIsSame;
        return isSame;
    }
}
