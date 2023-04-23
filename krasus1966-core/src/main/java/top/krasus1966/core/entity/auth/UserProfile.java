package top.krasus1966.core.entity.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户档案数据
 *
 * @author Krasus1966
 * @date 2022/11/17 11:31
 **/
@Data
public class UserProfile implements Serializable {
    private String id = "";

    private String tenantId = "";

    private String userName = "";

    private String realName = "";

    private String loginIp = "";

    private long loginTime;

    private String token = "";

    private String orgId = "";
    private String orgName = "";

    private String deptId = "";
    private String deptName = "";

    private String phoneNumber = "";

    private String idCard = "";

    private String sex = "";

    /**
     * 是否完善信息
     */
    private String improvedInfo = "";

    /**
     * 菜单
     */
    private String menu = "";

    /**
     * 按钮
     */
    private String button = "";

    private Map<String, Object> infos = new HashMap<>();
}
