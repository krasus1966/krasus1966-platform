package top.krasus1966.common.db.plugins.auto_create_table.entity;

import org.apache.ibatis.type.JdbcType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author krasus1966
 * @date 2024/9/12 18:36
 **/
public enum FieldToJdbcType {
    String("java.lang.String", "VARCHAR", true, JdbcType.VARCHAR, JdbcType.CHAR, JdbcType.LONGVARCHAR),
    Boolean("java.lang.Boolean", "BOOLEAN", false, JdbcType.BOOLEAN, JdbcType.BIT),
    Boolean2("boolean", "BOOLEAN", false, JdbcType.BOOLEAN, JdbcType.BIT),
    Byte("java.lang.byte[]", "BINARY", true, JdbcType.BINARY, JdbcType.VARBINARY, JdbcType.LONGVARBINARY),
    Bytes("java.lang.Byte", "TINYINT", false, JdbcType.TINYINT),
    Short("short", "SMALLINT", false, JdbcType.SMALLINT),
    Short2("java.lang.Short", "SMALLINT", false, JdbcType.SMALLINT),
    Long("java.lang.Long", "BIGINT", false, JdbcType.BIGINT),
    Integer("java.lang.Integer", "INT", false, JdbcType.INTEGER),
    BigInteger("java.lang.BigInteger", "BIGINT", false, JdbcType.BIGINT),
    Float("java.lang.Float", "DECIMAL", true, JdbcType.DOUBLE, JdbcType.REAL),
    Double("java.lang.Double", "DECIMAL", true, JdbcType.DOUBLE),
    BigDecimal("java.math.BigDecimal", "DECIMAL", true, JdbcType.DECIMAL, JdbcType.NUMERIC),
    Date("java.util.Date", "DATETIME", false, JdbcType.DATE, JdbcType.TIMESTAMP),
    LocalDate("java.time.LocalDate", "DATETIME", false, JdbcType.DATE, JdbcType.TIMESTAMP),
    LocalTime("java.time.LocalTime", "TIME", false, JdbcType.TIME),
    LocalDateTime("java.time.LocalDateTime", "DATETIME", false, JdbcType.DATE, JdbcType.TIMESTAMP),
    ;
    private static final Map<String, FieldToJdbcType> VALUES_MAP = new HashMap<>();

    static {
        for (FieldToJdbcType value : values()) {
            VALUES_MAP.put(value.JAVA_CLASS_NAME, value);
        }
    }

    public final String JAVA_CLASS_NAME;
    public final String DB_TYPE;
    public final boolean NEED_LENGTH;
    public final JdbcType[] JDBC_TYPE;

    FieldToJdbcType(String javaClassName, java.lang.String dbType, boolean needLength, JdbcType... jdbcType) {
        this.JAVA_CLASS_NAME = javaClassName;
        DB_TYPE = dbType;
        this.NEED_LENGTH = needLength;
        this.JDBC_TYPE = jdbcType;
    }

    public static JdbcType[] fromJavaClassName(String javaClassName) {
        FieldToJdbcType fieldToJdbcType = VALUES_MAP.get(javaClassName);
        if (null == fieldToJdbcType) {
            return null;
        }
        return fieldToJdbcType.JDBC_TYPE;
    }

    public static FieldToJdbcType getFieldToJdbcType(String javaClassName) {
        return VALUES_MAP.get(javaClassName);
    }
}
