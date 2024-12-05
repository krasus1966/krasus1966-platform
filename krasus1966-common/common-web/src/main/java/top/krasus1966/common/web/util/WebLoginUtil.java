package top.krasus1966.common.web.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import top.krasus1966.common.core.cache.util.CacheUtil;
import top.krasus1966.common.core.constant.LoginCacheConstant;
import top.krasus1966.common.core.constant.LoginConstants;
import top.krasus1966.common.core.entity.UserLoginInfo;
import top.krasus1966.common.core.util.AbstractLoginUtil;
import top.krasus1966.common.core.util.SpringUtil;

/**
 * @author krasus1966
 * @date 2024/12/5 10:58
 **/
@Component
public class WebLoginUtil extends AbstractLoginUtil {

    private static final LoginConstants LOGIN_CONSTANTS = SpringUtil.getBean(LoginConstants.class);

    @Override
    public String getToken() {
        HttpServletRequest request = ServletUtils.getRequest();
        return request.getHeader(LOGIN_CONSTANTS.getHeaderUserToken());
    }

    @Override
    public void setLoginUserInfo(UserLoginInfo userInfo) {
        // 如果当前用户存在登录信息，删除
        String oldToken =
                CacheUtil.get(LoginCacheConstant.USER_TOKEN + userInfo.getTenantId() + ":" + userInfo.getId());
        if (null != oldToken && !oldToken.isEmpty()) {
            CacheUtil.del(LoginCacheConstant.USER_INFO + oldToken);
            CacheUtil.del(LoginCacheConstant.USER_TOKEN + userInfo.getTenantId() + ":" + userInfo.getId());
        }

        // 重新保存新的token和用户信息
        CacheUtil.hset(LoginCacheConstant.USER_INFO + userInfo.getToken(), userInfo.toMap(),
                LOGIN_CONSTANTS.getExpireTimeLogin());
        CacheUtil.set(LoginCacheConstant.USER_TOKEN + userInfo.getTenantId() + ":" + userInfo.getId()
                , userInfo.getToken(), LOGIN_CONSTANTS.getExpireTimeLogin());
    }
}
