package top.krasus1966.common.rule_engine.service;

import top.krasus1966.common.rule_engine.entity.PageResultDTO;
import top.krasus1966.common.rule_engine.entity.Rule;
import top.krasus1966.common.rule_engine.entity.RuleScript;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/7/9 15:41
 **/
public interface IRuleStoreService {

    boolean hasRuleKey(String key);

    boolean hasRuleScriptKey(String key);

    boolean saveRule(Rule rule);
    boolean saveScript(RuleScript script);

    List<Rule> queryRules(Rule rule);

    List<RuleScript> queryRuleScripts(RuleScript script);

    PageResultDTO<Rule> queryPageRules(Rule rule, Integer page, Integer pageSize);

    PageResultDTO<RuleScript> queryPageRuleScripts(RuleScript script, Integer page, Integer pageSize);
}
