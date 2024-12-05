package top.krasus1966.common.core.util;

import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.stereotype.Component;
import top.krasus1966.common.core.cache.CacheFactory;
import top.krasus1966.common.core.constant.LoginCacheConstant;
import top.krasus1966.common.core.entity.UserLoginInfo;

import java.util.Map;
import java.util.function.Function;

public abstract class AbstractLoginUtil {

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
        Map<String, String> infoMap = CacheFactory.getCache().hget(LoginCacheConstant.USER_INFO + token.trim());
        if (null == infoMap || infoMap.isEmpty()) {
            return null;
        }
        return UserLoginInfo.toInfo(infoMap);
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
    public static boolean logout(Function<Void, String> getToken) {
        String token = getToken.apply(null);
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
            CacheFactory.getCache().del(LoginCacheConstant.USER_INFO + token);
            // 删除token信息
            CacheFactory.getCache().del(LoginCacheConstant.USER_TOKEN + info.getTenantId() + ":" + info.getId());
        }
        return true;
    }

    /**
     * 从header中获取token
     *
     * @return java.lang.String
     * @method getToken
     * @author krasus1966
     * @date 2022/4/15 22:09
     * @description 从header中获取token
     */
    public abstract String getToken();

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
    public abstract void setLoginUserInfo(UserLoginInfo userInfo);

    /**
     * 获得登录信息某字段
     *
     * @return java.lang.String
     * @method getLoginIp
     * @author krasus1966
     * @date 2022/4/15 22:10
     * @description 获得登录信息某字段
     */
    public String getOneInfo(String token, String field) {
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        return CacheFactory.getCache().hget(LoginCacheConstant.USER_INFO + token.trim(), field);
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
    public boolean isLogin() {
        UserLoginInfo info = getUserLoginInfo();
        if (null == info) {
            return false;
        }
        String redisToken =
                CacheFactory.getCache().get(LoginCacheConstant.USER_TOKEN + info.getTenantId() + ":" + info.getId());
        // 当前用户登录缓存token和缓存用户信息中的token不一致，应删除缓存用户信息
        if (!redisToken.equals(info.getToken())) {
            CacheFactory.getCache().del(LoginCacheConstant.USER_INFO + info.getToken());
            return false;
        }
        return true;
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
    public UserLoginInfo getUserLoginInfo() {
        String token = getToken();
        return getUserLoginInfo(token);
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
    public String getUserLoginId() {
        String token = getToken();
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        return CacheFactory.getCache().hget(LoginCacheConstant.USER_INFO + token.trim(), "id");
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
    public String getLoginIp() {
        String token = getToken();
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        return CacheFactory.getCache().hget(LoginCacheConstant.USER_INFO + token.trim(), "loginIp");
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
    public String getOneInfo(String field) {
        String token = getToken();
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        return CacheFactory.getCache().hget(LoginCacheConstant.USER_INFO + token.trim(), field);
    }
}
