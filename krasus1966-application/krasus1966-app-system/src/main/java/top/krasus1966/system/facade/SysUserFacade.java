package top.krasus1966.system.facade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.facade.AbstractCrudFacade;
import top.krasus1966.system.domain.form.SysUserSearchForm;
import top.krasus1966.system.domain.form.SysUserUpdateForm;
import top.krasus1966.system.domain.persistent.SysUser;
import top.krasus1966.system.domain.response.SysUserResponse;
import top.krasus1966.system.service.SysUserBaseServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:44
 **/
@RestController
@RequestMapping("/admin/sys-user")
public class SysUserFacade extends AbstractCrudFacade<SysUserBaseServiceImpl, SysUser, SysUserResponse, SysUserUpdateForm, SysUserSearchForm> {
    public SysUserFacade(HttpServletRequest request, HttpServletResponse response, SysUserBaseServiceImpl service, IRuleExecuteService ruleExecuteService) {
        super(request, response, service,ruleExecuteService);
    }
}
