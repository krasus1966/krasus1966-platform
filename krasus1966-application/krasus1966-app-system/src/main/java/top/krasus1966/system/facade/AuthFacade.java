package top.krasus1966.system.facade;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.web.auth.entity.LoginDTO;
import top.krasus1966.core.web.auth.entity.UserLoginInfo;
import top.krasus1966.core.web.entity.R;

/**
 * @author Krasus1966
 * @date 2023/5/27 21:22
 **/
@RestController
@RequestMapping("/auth")
public class AuthFacade {

    @PostMapping("/loginNoEncode")
    public R<UserLoginInfo> loginNoEncode(@RequestBody LoginDTO vo) {
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setId("1");
        userLoginInfo.setUserName("admin");
        userLoginInfo.setRealName("管理员");
        userLoginInfo.setToken("token123123");
        userLoginInfo.setTenantId("tenantId1");
        return R.success(userLoginInfo);
    }
}