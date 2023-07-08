package top.krasus1966.liteflow.facade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.facade.AbstractCrudFacade;
import top.krasus1966.liteflow.entity.form.search.LiteflowScriptSearchForm;
import top.krasus1966.liteflow.entity.form.update.LiteflowScriptUpdateForm;
import top.krasus1966.liteflow.entity.persistent.LiteflowScriptPersistent;
import top.krasus1966.liteflow.entity.response.LiteflowScriptResponse;
import top.krasus1966.liteflow.mybatis.service.LiteflowScriptServiceImpl;

/**
 * @author Krasus1966
 * @date 2023/7/8 22:50
 **/
@RestController
@RequestMapping("/admin/liteflow-script")
public class LiteflowScriptFacade extends AbstractCrudFacade<LiteflowScriptServiceImpl,
        LiteflowScriptPersistent,
        LiteflowScriptResponse,
        LiteflowScriptUpdateForm,
        LiteflowScriptSearchForm> {
    public LiteflowScriptFacade(HttpServletRequest request, HttpServletResponse response, LiteflowScriptServiceImpl service, IRuleExecuteService ruleExecuteService) {
        super(request, response, service,ruleExecuteService);
    }
}
