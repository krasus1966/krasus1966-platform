package top.krasus1966.system.facade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.facade.AbstractAdminFacade;
import top.krasus1966.system.domain.sys.SysAppVersion;
import top.krasus1966.system.service.SysAppVersionService;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:44
 **/
@RestController
@RequestMapping("/admin/sys-app-version")
public class SysAppVersionFacade extends AbstractAdminFacade<SysAppVersionService, SysAppVersion> {

    public SysAppVersionFacade(HttpServletRequest request, HttpServletResponse response, SysAppVersionService service, IRuleExecuteService ruleExecuteService) {
        super(request, response, service, ruleExecuteService);
    }


}
