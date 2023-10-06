package top.krasus1966.core.web.constant;

import top.krasus1966.core.cache.constant.Cache;

/**
 * 登录相关的常量配置
 *
 * @author Krasus1966
 * @date 2022/10/31 14:16
 **/
public interface LoginConst {
    /**
     * 验证码
     */
    String CAPTCHA_KEY = Cache.Prefix.CACHE_STR + "CAPTCHA:";
    /**
     * 短信验证码
     */
    String MOBILE_SMSCODE = Cache.Prefix.CACHE_STR + "MOBILE:SMSCODE:";

    /**
     * 缓存内token
     */
    String USER_TOKEN = Cache.Prefix.CACHE_LOGIN + "USER_TOKEN:";

    /**
     * 缓存内用户信息
     */
    String USER_INFO = Cache.Prefix.CACHE_LOGIN + "USER_INFO:";

    /**
     * 用户id
     */
    String USER_ID = Cache.Prefix.CACHE_LOGIN + "USER_ID:";

    /**
     * 登录失败次数
     */
    String FAIL_USER_ID = Cache.Prefix.CACHE_LOGIN + "FAIL_USER_ID:";

    /**
     * 锁定用户
     */
    String LOCK_USER_ID = Cache.Prefix.CACHE_LOGIN + "LOCK_USER_ID:";

    /**
     * 缓存内用户菜单列表权限
     */
    String USER_MENU = Cache.Prefix.CACHE_LOGIN + "AUTH_MENU";
    String INFO_USER_MENUS = "MENUS";

    /**
     * 缓存内用户按钮权限
     */
    String USER_BUTTON = Cache.Prefix.CACHE_LOGIN + "AUTH_BUTTON";
    String INFO_USER_BUTTONS = "BUTTONS";
    /**
     * 缓存内用户接口权限
     */
    String USER_INTERFACE = Cache.Prefix.CACHE_LOGIN + "AUTH_FACADE";

    /**
     * 缓存内对称密钥
     */
    String USER_AES_KEYS = Cache.Prefix.CACHE_LOGIN + "NEW_KEY";
    String INFO_USER_AES_KEY = "NEW_KEY";
}
