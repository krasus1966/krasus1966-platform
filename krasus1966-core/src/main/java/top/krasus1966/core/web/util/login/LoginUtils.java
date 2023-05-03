package top.krasus1966.core.web.util.login;

import cn.hutool.core.text.CharSequenceUtil;
import top.krasus1966.core.base.constant.LoginConstants;
import top.krasus1966.core.web.auth.entity.UserLoginInfo;
import top.krasus1966.core.cache.redis_util.CacheUtil;
import top.krasus1966.core.web.util.servlet.ServletUtils;
import top.krasus1966.core.spring.util.SpringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2022/10/31 17:25
 **/
public class LoginUtils {

    private static final LoginConstants LOGIN_CONSTANTS =
            SpringUtil.getBean(LoginConstants.class);

    /**
     * 从header中获取token
     *
     * @return java.lang.String
     * @method getToken
     * @author krasus1966
     * @date 2022/4/15 22:09
     * @description 从header中获取token
     */
    public static String getToken() {
        HttpServletRequest request = ServletUtils.getRequest();
        if (null == request) {
            return null;
        }
        return request.getHeader(LOGIN_CONSTANTS.getHeaderUserToken());
    }

    /**
     * 判断是否登录
     * 检查缓存内是否存在token
     *
     * @return boolean
     * @method isLogin
     * @author krasus1966
     * @date 2022/4/15 22:08
     * @description 判断是否登录
     */
    public static boolean isLogin() {
        UserLoginInfo info = getUserLoginInfo();
        if (null == info) {
            return false;
        }
        String redisToken =
                CacheUtil.get(top.krasus1966.core.web.constant.LoginConstants.USER_TOKEN + info.getTenantId() + ":" + info.getId());
        // 当前用户登录缓存token和缓存用户信息中的token不一致，应删除缓存用户信息
        if (!redisToken.equals(info.getToken())) {
            CacheUtil.del(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + info.getToken());
            return false;
        }
        return true;
    }

    /**
     * 设置登录信息
     *
     * @param userInfo 用户信息
     * @return void
     * @method setLoginUserInfo
     * @author krasus1966
     * @date 2022/4/15 22:09
     * @description 设置登录信息
     */
    public static void setLoginUserInfo(UserLoginInfo userInfo) {
        // 如果当前用户存在登录信息，删除
        String oldToken =
                CacheUtil.get(top.krasus1966.core.web.constant.LoginConstants.USER_TOKEN + userInfo.getTenantId() + ":" + userInfo.getId());
        if (null != oldToken && oldToken.length() > 0) {
            CacheUtil.del(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + oldToken);
            CacheUtil.del(top.krasus1966.core.web.constant.LoginConstants.USER_TOKEN + userInfo.getTenantId() + ":" + userInfo.getId());
        }

        // 重新保存新的token和用户信息
        CacheUtil.hset(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + userInfo.getToken(), userInfo.toMap(),
                LOGIN_CONSTANTS.getExpireTimeLogin());
        CacheUtil.set(top.krasus1966.core.web.constant.LoginConstants.USER_TOKEN + userInfo.getTenantId() + ":" + userInfo.getId()
                , userInfo.getToken(), LOGIN_CONSTANTS.getExpireTimeLogin());
    }

    /**
     * 获得登录用户信息
     *
     * @return top.krasus1966.base.common.entity.user.UserLoginInfo
     * @method getUserLoginInfo
     * @author krasus1966
     * @date 2022/4/15 22:10
     * @description 获得登录用户信息
     */
    public static UserLoginInfo getUserLoginInfo() {
        String token = getToken();
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        Map<String, String> infoMap = CacheUtil.hget(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token.trim());
        if (null == infoMap || infoMap.isEmpty()) {
            return null;
        }
        return UserLoginInfo.toInfo(infoMap);
    }

    /**
     * 获得登录用户信息
     *
     * @return top.krasus1966.base.common.entity.user.UserLoginInfo
     * @method getUserLoginInfo
     * @author krasus1966
     * @date 2022/4/15 22:10
     * @description 获得登录用户信息
     */
    public static UserLoginInfo getUserLoginInfo(String token) {
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        Map<String, String> infoMap = CacheUtil.hget(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token.trim());
        if (null == infoMap || infoMap.isEmpty()) {
            return null;
        }
        return UserLoginInfo.toInfo(infoMap);
    }

    /**
     * 获得登录人id
     *
     * @return java.lang.String
     * @method getUserLoginId
     * @author krasus1966
     * @date 2022/4/15 22:10
     * @description 获得登录人id
     */
    public static String getUserLoginId() {
        String token = getToken();
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        return CacheUtil.hget(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token.trim(), "id");
    }

    /**
     * 获得登录人ip地址
     *
     * @return java.lang.String
     * @method getLoginIp
     * @author krasus1966
     * @date 2022/4/15 22:10
     * @description 获得登录人ip地址
     */
    public static String getLoginIp() {
        String token = getToken();
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        return CacheUtil.hget(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token.trim(), "loginIp");
    }

    /**
     * 获得登录信息某字段
     *
     * @return java.lang.String
     * @method getLoginIp
     * @author krasus1966
     * @date 2022/4/15 22:10
     * @description 获得登录信息某字段
     */
    public static String getOneInfo(String field) {
        String token = getToken();
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        return CacheUtil.hget(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token.trim(), field);
    }

    /**
     * 获得登录信息某字段
     *
     * @return java.lang.String
     * @method getLoginIp
     * @author krasus1966
     * @date 2022/4/15 22:10
     * @description 获得登录信息某字段
     */
    public static String getOneInfo(String token, String field) {
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        return CacheUtil.hget(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token.trim(), field);
    }

    /**
     * 从header获得token并登出
     *
     * @return boolean
     * @method logout
     * @author krasus1966
     * @date 2022/4/15 22:10
     * @description 登出，从header获得token
     */
    public static boolean logout() {
        String token = getToken();
        return logout(token);
    }

    /**
     * 登出，手动传token
     *
     * @param token 用户token
     * @return boolean
     * @method logout
     * @author krasus1966
     * @date 2022/4/15 22:11
     * @description 登出，手动传token
     */
    public static boolean logout(String token) {
        UserLoginInfo info = getUserLoginInfo(token);
        if (null != info) {
            // 删除缓存用户信息
            CacheUtil.del(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token);
            // 删除token信息
            CacheUtil.del(top.krasus1966.core.web.constant.LoginConstants.USER_TOKEN + info.getTenantId() + ":" + info.getId());
        }
        return true;
    }
}
