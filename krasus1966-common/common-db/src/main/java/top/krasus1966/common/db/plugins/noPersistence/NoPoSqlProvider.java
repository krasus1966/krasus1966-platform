package top.krasus1966.common.db.plugins.noPersistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import top.krasus1966.common.core.entity.UserLoginInfo;
import top.krasus1966.common.core.util.AbstractLoginUtil;
import top.krasus1966.common.core.util.SpringUtil;

import java.time.LocalDateTime;
import java.util.*;

public class NoPoSqlProvider {

    private static void setDefaultInsert(Map<String, Object> data, Optional<UserLoginInfo> userLoginInfoOptional) {
        data.put("id", UUID.randomUUID().toString());
        data.put("deleted", 0);
        data.put("crtTime", LocalDateTime.now());
        if (userLoginInfoOptional.isPresent()) {
            data.put("crtId", userLoginInfoOptional.get().getId());
            data.put("crtIp", userLoginInfoOptional.get().getLoginIp());
            data.put("crtDeptId", userLoginInfoOptional.get().getDeptId());
        }
    }

    private static void setDefaultUpdate(Map<String, Object> data, Optional<UserLoginInfo> userLoginInfoOptional) {
        data.put("updTime", LocalDateTime.now());
        if (userLoginInfoOptional.isPresent()) {
            data.put("updId", userLoginInfoOptional.get().getId());
            data.put("updIp", userLoginInfoOptional.get().getLoginIp());
            data.put("updDeptId", userLoginInfoOptional.get().getDeptId());
        }
    }

    public String insertNoPO(String tableName, Map<String, Object> data) {
        Optional<UserLoginInfo> userLoginInfoOptional =
                Optional.ofNullable(SpringUtil.getBean(AbstractLoginUtil.class).getUserLoginInfo());

        // 设置新增默认值
        setDefaultInsert(data, userLoginInfoOptional);

        // 生成参数
        SQL sql = new SQL();
        sql.INSERT_INTO(tableName);
        for (String key : data.keySet()) {
            sql.VALUES(StringUtils.camelToUnderline(key), "#{data." + key + "}");
        }
        return sql.toString();
    }

    public String batchInsertNoPO(String tableName, Set<String> fieldSet, List<Map<String, Object>> dataList) {
        Optional<UserLoginInfo> userLoginInfoOptional =
                Optional.ofNullable(SpringUtil.getBean(AbstractLoginUtil.class).getUserLoginInfo());

        // 补充默认值字段
        fieldSet.add("id");
        fieldSet.add("deleted");
        fieldSet.add("crtId");
        fieldSet.add("crtIp");
        fieldSet.add("crtTime");
        fieldSet.add("crtDeptId");

        // 设置新增默认值
        for (Map<String, Object> data : dataList) {
            setDefaultInsert(data, userLoginInfoOptional);
        }

        SQL sql = new SQL() {{
            INSERT_INTO(tableName);
            INTO_COLUMNS(fieldSet.stream().map(StringUtils::camelToUnderline).toArray(String[]::new));
            for (int i = 0; i < dataList.size(); i++) {
                int finalI = i;
                INTO_VALUES(fieldSet.stream().map(key -> "#{dataList[" + finalI + "]." + key + "}").toArray(String[]::new));
                ADD_ROW();
            }
        }};
        return sql.toString();
    }

    public String updateNoPOById(String tableName, String id, Map<String, Object> data) {
        Optional<UserLoginInfo> userLoginInfoOptional =
                Optional.ofNullable(SpringUtil.getBean(AbstractLoginUtil.class).getUserLoginInfo());

        // 设置修改默认值
        setDefaultUpdate(data, userLoginInfoOptional);

        // 设置参数
        SQL sql = new SQL() {{
            UPDATE(tableName);
            SET(data.keySet().stream()
                    .map(key -> StringUtils.camelToUnderline(key) + " = #{data." + key + "}")
                    .toArray(String[]::new));
            WHERE("id=#{id} AND deleted = 0");
        }};
        return sql.toString();
    }

    public String selectByNoPo(String tableName,
                               @Param("ew") QueryWrapper<Map<String, Object>> ew) {
        SqlMethod sqlMethod = SqlMethod.SELECT_LIST;
        ew.eq("deleted", 0);
        return String.format(sqlMethod.getSql(), ew.getSqlFirst() != null ? ew.getSqlFirst() : "",
                ew.getSqlSelect() != null ? ew.getSqlSelect() : "*",
                tableName, ew.getCustomSqlSegment(),
                sqlOrderBy(), sqlComment());
    }

    public String countByNoPo(String tableName,
                              @Param("ew") QueryWrapper<Map<String, Object>> ew) {
        SqlMethod sqlMethod = SqlMethod.SELECT_COUNT;
        ew.eq("deleted", 0);
        return String.format(sqlMethod.getSql(), ew.getSqlFirst() != null ? ew.getSqlFirst() : "",
                ew.getSqlSelect() != null ? ew.getSqlSelect() : "*",
                tableName, ew.getCustomSqlSegment(),
                sqlOrderBy(), sqlComment());
    }

    protected String sqlOrderBy() {
        return SqlScriptUtils.convertIf("", String.format("%s == null or %s", Constants.WRAPPER,
                Constants.WRAPPER_EXPRESSION_ORDER), true);
    }

    protected String sqlComment() {
        return Constants.NEWLINE + convertIfEwParam(Constants.Q_WRAPPER_SQL_COMMENT, true);
    }

    protected String convertIfEwParam(final String param, final boolean newLine) {
        return SqlScriptUtils.convertIf(SqlScriptUtils.unSafeParam(param),
                String.format("%s != null and %s != null", Constants.WRAPPER, param), newLine);
    }
}
