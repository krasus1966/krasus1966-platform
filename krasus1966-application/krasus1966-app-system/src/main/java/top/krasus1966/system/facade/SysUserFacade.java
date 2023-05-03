package top.krasus1966.system.facade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.web.facade.AdminBaseController;
import top.krasus1966.system.domain.SysUser;
import top.krasus1966.system.service.SysUserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:44
 **/
@RestController
@RequestMapping("/admin/sys-user")
public class SysUserFacade extends AdminBaseController<SysUserServiceImpl, SysUser> {
    public SysUserFacade(HttpServletRequest request, HttpServletResponse response, SysUserServiceImpl service) {
        super(request, response, service);
    }
}
