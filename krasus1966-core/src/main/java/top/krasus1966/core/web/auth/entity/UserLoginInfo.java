package top.krasus1966.core.web.auth.entity;


import top.krasus1966.core.web.exception.WrongFieldThroble;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2022/10/31 17:23
 **/
public class UserLoginInfo implements Serializable {
    private static final long serialVersionUID = 1865295604492056213L;

    private String id = "";

    private String tenantId = "";

    private String userName = "";

    private String realName = "";

    private String loginIp = "";

    private long loginTime;

    private String token = "";

    private Map<String, String> infos = new HashMap<>();

    public UserLoginInfo() {
    }

    /**
     * 用户缓存信息转用户登录信息
     *
     * @param map 缓存信息返回的hashMap
     * @return top.krasus1966.base_project.common.core.entity.auth.UserLoginInfo
     * @method toInfo
     * @author krasus1966
     * @date 2022/10/31 17:50
     * @description 用户缓存信息转用户登录信息
     */
    public static UserLoginInfo toInfo(Map<String, String> map) {
        UserLoginInfo info = new UserLoginInfo();
        map.forEach((key, value) -> {
            if ("id".equals(key)) {
                info.setId(value);
            } else if ("tenantId".equals(key)) {
                info.setTenantId(value);
            } else if ("userName".equals(key)) {
                info.setUserName(value);
            } else if ("realName".equals(key)) {
                info.setRealName(value);
            } else if ("loginIp".equals(key)) {
                info.setLoginIp(value);
            } else if ("loginTime".equals(key)) {
                info.setLoginTime(Long.parseLong(value));
            } else if ("token".equals(key)) {
                info.setToken(value);
            } else {
                try {
                    info.addOneInfo(key, value);
                } catch (WrongFieldThroble e) {
                    // 正常加不会走
                    throw new RuntimeException(e);
                }
            }
        });
        return info;
    }

    public String getId() {
        return id;
    }

    public UserLoginInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public UserLoginInfo setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserLoginInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public UserLoginInfo setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public UserLoginInfo setLoginIp(String loginIp) {
        this.loginIp = loginIp;
        return this;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public UserLoginInfo setLoginTime(long loginTime) {
        this.loginTime = loginTime;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserLoginInfo setToken(String token) {
        this.token = token;
        return this;
    }

    public Map<String, String> getInfos() {
        return infos;
    }

    public UserLoginInfo setInfos(Map<String, String> infos) throws WrongFieldThroble {
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            checkExtraInfoKey(key);
        }
        this.infos = infos;
        return this;
    }

    public UserLoginInfo addOneInfo(String name, String value) throws WrongFieldThroble {
        checkExtraInfoKey(name);
        this.infos.put(name, value);
        return this;
    }

    private void checkExtraInfoKey(String key) throws WrongFieldThroble {
        if ("id".equals(key)) {
            throw new WrongFieldThroble("id不能存储在额外信息中");
        }
        if ("tenantId".equals(key)) {
            throw new WrongFieldThroble("tenantId不能存储在额外信息中");
        }
        if ("userName".equals(key)) {
            throw new WrongFieldThroble("userName不能存储在额外信息中");
        }
        if ("realName".equals(key)) {
            throw new WrongFieldThroble("realName不能存储在额外信息中");
        }
        if ("loginIp".equals(key)) {
            throw new WrongFieldThroble("loginIp不能存储在额外信息中");
        }
        if ("loginTime".equals(key)) {
            throw new WrongFieldThroble("loginTime不能存储在额外信息中");
        }
        if ("token".equals(key)) {
            throw new WrongFieldThroble("token不能存储在额外信息中");
        }
    }

    /**
     * 用户信息转map
     *
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @method toMap
     * @author krasus1966
     * @date 2022/10/31 17:50
     * @description 用户信息转map
     */
    public Map<String, String> toMap() {
        infos.put("id", id);
        infos.put("tenantId", tenantId);
        infos.put("userName", userName);
        infos.put("realName", realName);
        infos.put("loginIp", loginIp);
        infos.put("loginTime", String.valueOf(loginTime));
        infos.put("token", token);
        return infos;
    }
}
