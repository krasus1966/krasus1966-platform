package top.krasus1966.liteflow.facade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.facade.AbstractCrudFacade;
import top.krasus1966.liteflow.entity.form.search.LiteflowChainSearchForm;
import top.krasus1966.liteflow.entity.form.update.LiteflowChainUpdateForm;
import top.krasus1966.liteflow.entity.persistent.LiteflowChainPersistent;
import top.krasus1966.liteflow.entity.response.LiteflowChainResponse;
import top.krasus1966.liteflow.mybatis.service.LiteflowChainServiceImpl;

/**
 * @author Krasus1966
 * @date 2023/7/8 22:50
 **/
@RestController
@RequestMapping("/admin/liteflow-chain")
public class LiteflowChainFacade extends AbstractCrudFacade<LiteflowChainServiceImpl,
        LiteflowChainPersistent,
        LiteflowChainResponse,
        LiteflowChainUpdateForm,
        LiteflowChainSearchForm> {
    public LiteflowChainFacade(HttpServletRequest request, HttpServletResponse response, LiteflowChainServiceImpl service, IRuleExecuteService ruleExecuteService) {
        super(request, response, service,ruleExecuteService);
    }
}
