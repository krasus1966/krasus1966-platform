package top.krasus1966.system.facade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.web.facade.AbstractCrudFacade;
import top.krasus1966.system.domain.form.SysUserSearchForm;
import top.krasus1966.system.domain.form.SysUserUpdateForm;
import top.krasus1966.system.domain.persistent.SysUser;
import top.krasus1966.system.domain.response.SysUserResponse;
import top.krasus1966.system.service.SysUserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:44
 **/
@RestController
@RequestMapping("/admin/sys-user")
public class SysUserFacade extends AbstractCrudFacade<SysUserServiceImpl, SysUser, SysUserResponse, SysUserUpdateForm, SysUserSearchForm> {
    public SysUserFacade(HttpServletRequest request, HttpServletResponse response, SysUserServiceImpl service) {
        super(request, response, service);
    }
}
