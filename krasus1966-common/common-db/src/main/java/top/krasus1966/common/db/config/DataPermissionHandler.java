package top.krasus1966.common.db.config;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import top.krasus1966.common.core.cache.CacheFactory;
import top.krasus1966.common.core.cache.ICache;
import top.krasus1966.common.core.constant.LoginCacheConstant;
import top.krasus1966.common.core.entity.UserLoginInfo;
import top.krasus1966.common.core.util.AbstractLoginUtil;
import top.krasus1966.common.core.util.SpringUtil;
import top.krasus1966.common.db.entity.TablePermission;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DataPermissionHandler implements MultiDataPermissionHandler {

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        AbstractLoginUtil loginUtil = SpringUtil.getBean(AbstractLoginUtil.class);
        ICache cache = CacheFactory.getCache();
        List<TablePermission> permissionPOList = null;
        try {
            permissionPOList = cache.getObject(LoginCacheConstant.DATA_PERMISSION_CACHE + table.getName(),
                    new TypeReference<>() {
                    });
            if (permissionPOList == null || permissionPOList.isEmpty()) {
                return null;
            }
            UserLoginInfo userLoginInfo = loginUtil.getUserLoginInfo();
            if (null == userLoginInfo) {
                return null;
            }
            permissionPOList =
                    permissionPOList.stream().sorted(Comparator.comparing(TablePermission::getSort)).toList();
            String roleIds = userLoginInfo.getRoleIds();
            for (TablePermission tablePermission : permissionPOList) {
                if (roleIds.contains(tablePermission.getRoleId())) {
                    return createTablePermissionSql(tablePermission);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Expression createTablePermissionSql(TablePermission tablePermission) {
        AbstractLoginUtil loginUtil = SpringUtil.getBean(AbstractLoginUtil.class);
        switch (tablePermission.getPermissionType()) {
            case TablePermission.SJQX_BR:
                return new EqualsTo(new Column(StringUtils.camelToUnderline(tablePermission.getCreatorColumn())),
                        new StringValue(loginUtil.getUserLoginId()));
            case TablePermission.SJQX_DEPT:
                return new EqualsTo(new Column(StringUtils.camelToUnderline(tablePermission.getDepartColumn())),
                        new StringValue(loginUtil.getUserLoginInfo().getDeptId()));
            case TablePermission.SJQX_DEPT_ALL:
                String deptFullIds = loginUtil.getUserLoginInfo().getDeptFullIds();
                ExpressionList<StringValue> expressionList =
                        new ExpressionList<>(Arrays.stream(deptFullIds.split(",")).map(StringValue::new).toList());
                return new InExpression(new Column(StringUtils.camelToUnderline(tablePermission.getDepartColumn())),
                        ParenthesedExpressionList.from(expressionList));
            default:
                return null;
        }
    }
}
