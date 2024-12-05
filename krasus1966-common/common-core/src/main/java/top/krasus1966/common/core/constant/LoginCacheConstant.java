package top.krasus1966.common.core.constant;

/**
 * 登录相关的常量配置
 *
 * @author Krasus1966
 * @date 2022/10/31 14:16
 **/
public interface LoginCacheConstant {
    /**
     * 验证码
     */
    String CAPTCHA_KEY = "CAPTCHA:";
    /**
     * 短信验证码
     */
    String MOBILE_SMSCODE = "MOBILE:SMSCODE:";

    /**
     * 缓存内token
     */
    String USER_TOKEN = "USER_TOKEN:";

    /**
     * 缓存内用户信息
     */
    String USER_INFO = "USER_INFO:";

    /**
     * 用户id
     */
    String USER_ID = "USER_ID:";

    /**
     * 登录失败次数
     */
    String FAIL_USER_ID = "FAIL_USER_ID:";

    /**
     * 锁定用户
     */
    String LOCK_USER_ID = "LOCK_USER_ID:";

    /**
     * 缓存内用户菜单列表权限
     */
    String USER_MENU = "AUTH_MENU";
    String INFO_USER_MENUS = "MENUS";

    /**
     * 缓存内用户按钮权限
     */
    String USER_BUTTON = "AUTH_BUTTON";
    String INFO_USER_BUTTONS = "BUTTONS";
    /**
     * 缓存内用户接口权限
     */
    String USER_INTERFACE = "AUTH_FACADE";

    /**
     * 缓存内对称密钥
     */
    String USER_AES_KEYS = "NEW_KEY";
    String INFO_USER_AES_KEY = "NEW_KEY";

    String DATA_PERMISSION_CACHE = "DATA_PERMISSION:";
}
