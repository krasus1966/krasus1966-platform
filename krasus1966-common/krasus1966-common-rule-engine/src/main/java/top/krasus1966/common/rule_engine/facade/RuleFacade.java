package top.krasus1966.common.rule_engine.facade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.krasus1966.common.rule_engine.entity.PageResultDTO;
import top.krasus1966.common.rule_engine.entity.Rule;
import top.krasus1966.common.rule_engine.entity.RuleScript;
import top.krasus1966.common.rule_engine.service.IRuleStoreService;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.facade.BaseController;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/7/9 15:53
 **/
@ResponseBody
@RequestMapping("/rule")
public class RuleFacade extends BaseController {
    private final IRuleStoreService ruleStoreService;
    private final IRuleExecuteService ruleExecuteService;

    public RuleFacade(IRuleStoreService ruleStoreService, IRuleExecuteService ruleExecuteService, HttpServletRequest request,
                      HttpServletResponse response) {
        super(request, response);
        this.ruleStoreService = ruleStoreService;
        this.ruleExecuteService = ruleExecuteService;
    }


    @PostMapping("/saveRule")
    public R saveRule(@RequestBody @Validated Rule rule) {
        boolean hasRuleKey = ruleExecuteService.hasRule(rule.getRuleKey());
        if (hasRuleKey) {
            return R.failed("key已存在");
        }
        if (!ruleExecuteService.validRule(rule.getRuleData())) {
            return R.failed("规则有误，请检查规则调用组件是否存在或规则是否正确");
        }
        boolean isSave = ruleStoreService.saveRule(rule);
        if (isSave) {
            ruleExecuteService.reloadRule();
        }
        return isSave ? R.success() : R.failed("保存失败");
    }

    @PostMapping("/saveScript")
    public R saveScript(@RequestBody @Validated RuleScript script) {
        boolean hasRuleKey = ruleStoreService.hasRuleScriptKey(script.getScriptKey());
        if (hasRuleKey) {
            return R.failed("key已存在");
        }
        boolean isSave = ruleStoreService.saveScript(script);
        if (isSave) {
            ruleExecuteService.reloadScript();
        }
        return isSave ? R.success() : R.failed("保存失败");
    }

    @GetMapping("/queryRule")
    public R<List<Rule>> queryRule(Rule rule) {
        return R.success(ruleStoreService.queryRules(rule));
    }

    @GetMapping("/queryRuleScript")
    public R<List<RuleScript>> queryRuleScript(RuleScript script) {
        return R.success(ruleStoreService.queryRuleScripts(script));
    }

    @GetMapping("/queryPageRule")
    public R<PageResultDTO<Rule>> queryPageRule(Rule rule, Integer page,
                                                Integer pageSize) {
        return R.success(ruleStoreService.queryPageRules(rule, page, pageSize));
    }

    @GetMapping("/queryPageRuleScript")
    public R<PageResultDTO<RuleScript>> queryPageRuleScript(RuleScript script, Integer page,
                                                            Integer pageSize) {
        return R.success(ruleStoreService.queryPageRuleScripts(script, page, pageSize));
    }
}
