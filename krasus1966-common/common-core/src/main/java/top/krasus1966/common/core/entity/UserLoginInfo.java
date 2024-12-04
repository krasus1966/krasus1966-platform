package top.krasus1966.common.core.entity;


import lombok.Data;
import top.krasus1966.common.core.exception.BizException;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2022/10/31 17:23
 **/
@Data
public class UserLoginInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1865295604492056213L;

    private String id = "";

    private String tenantId = "";

    private String userName = "";

    private String realName = "";

    private String loginIp = "";

    private String deptId = "";

    private String deptFullIds = "";

    private String roleIds = "";

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
            } else if ("deptId".equals(key)) {
                info.setDeptId(value);
            } else if ("deptFullIds".equals(key)) {
                info.setDeptFullIds(value);
            } else if ("roleIds".equals(key)) {
                info.setRoleIds(value);
            } else if ("loginIp".equals(key)) {
                info.setLoginIp(value);
            } else if ("loginTime".equals(key)) {
                info.setLoginTime(Long.parseLong(value));
            } else if ("token".equals(key)) {
                info.setToken(value);
            } else {
                try {
                    info.addOneInfo(key, value);
                } catch (BizException e) {
                    // 正常加不会走
                    throw new RuntimeException(e);
                }
            }
        });
        return info;
    }


    public UserLoginInfo setInfos(Map<String, String> infos) throws BizException {
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            checkExtraInfoKey(key);
        }
        this.infos = infos;
        return this;
    }

    public void addOneInfo(String name, String value) throws BizException {
        checkExtraInfoKey(name);
        this.infos.put(name, value);
    }

    private void checkExtraInfoKey(String key) throws BizException {
        if ("id".equals(key)) {
            throw new BizException("id不能存储在额外信息中");
        }
        if ("tenantId".equals(key)) {
            throw new BizException("tenantId不能存储在额外信息中");
        }
        if ("userName".equals(key)) {
            throw new BizException("userName不能存储在额外信息中");
        }
        if ("realName".equals(key)) {
            throw new BizException("realName不能存储在额外信息中");
        }
        if ("deptId".equals(key)) {
            throw new BizException("deptId不能存储在额外信息中");
        }
        if ("deptFullIds".equals(key)) {
            throw new BizException("deptFullIds不能存储在额外信息中");
        }
        if ("roleIds".equals(key)) {
            throw new BizException("roleIds不能存储在额外信息中");
        }
        if ("loginIp".equals(key)) {
            throw new BizException("loginIp不能存储在额外信息中");
        }
        if ("loginTime".equals(key)) {
            throw new BizException("loginTime不能存储在额外信息中");
        }
        if ("token".equals(key)) {
            throw new BizException("token不能存储在额外信息中");
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
        infos.put("deptId", deptId);
        infos.put("deptFullIds", deptFullIds);
        infos.put("roleIds", deptFullIds);
        infos.put("loginIp", loginIp);
        infos.put("loginTime", String.valueOf(loginTime));
        infos.put("token", token);
        return infos;
    }
}
