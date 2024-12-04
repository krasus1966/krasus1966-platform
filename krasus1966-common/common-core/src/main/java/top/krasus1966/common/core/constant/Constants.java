package top.krasus1966.common.core.constant;


import top.krasus1966.common.core.util.I18NUtils;

/**
 * @author Krasus1966
 * @date 2021/6/9 17:48
 **/
public interface Constants {

    interface Result {
        String ACTION_OK = I18NUtils.getMessage("action.success", "操作成功");
        String ACTION_FAILED = I18NUtils.getMessage("action.failed", "操作失败");
    }

    interface RequestHeader {
        String TENANT_ID_NAME = "tenantId";
    }
}
