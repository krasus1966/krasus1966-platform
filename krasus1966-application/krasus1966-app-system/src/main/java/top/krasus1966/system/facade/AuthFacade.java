package top.krasus1966.system.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.web.auth.entity.LoginDTO;
import top.krasus1966.core.web.auth.entity.LoginVO;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.exception.WrongFieldThroble;
import top.krasus1966.system.domain.sys_user.SysUser;
import top.krasus1966.system.service.LoginService;

/**
 * @author Krasus1966
 * @date 2023/5/27 21:22
 **/
@RestController
@RequestMapping("/auth")
public class AuthFacade {

    private final LoginService loginService;

    public AuthFacade(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 登录
     *
     * @param param 加密参数

     * @throws WrongFieldThroble       字段存储位置异常
     * @throws JsonProcessingException json转换失败异常
     * @method loginByAccount
     * @author krasus1966
     * @date 2022/11/17 21:32
     * @description 登录
     */
    @PostMapping("/login")
    public R<LoginVO> login(@Validated @NotBlank(message = "非法参数") String param) throws WrongFieldThroble, JsonProcessingException {
        LoginDTO loginDTO = loginService.checkLoginParam(param);
        return R.success(loginService.login(loginDTO));
    }

    /**
     * 获取登录用户信息
     *
     * @method profile
     * @author krasus1966
     * @date 2022/11/17 21:36
     * @description 获取登录用户信息
     */
    @ApiOperation(value = "获取登录用户信息")
    @RequestMapping("/profile")
    public R<SysUser> profile() {
        return R.success(loginService.profile());
    }
}