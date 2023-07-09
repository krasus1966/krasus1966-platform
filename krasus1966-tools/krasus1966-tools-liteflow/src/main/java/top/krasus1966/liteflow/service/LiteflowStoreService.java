package top.krasus1966.liteflow.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import org.springframework.stereotype.Service;
import top.krasus1966.common.rule_engine.entity.PageResultDTO;
import top.krasus1966.common.rule_engine.entity.Rule;
import top.krasus1966.common.rule_engine.entity.RuleScript;
import top.krasus1966.common.rule_engine.service.IRuleStoreService;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.liteflow.entity.LiteflowChainPersistent;
import top.krasus1966.liteflow.entity.LiteflowScriptPersistent;
import top.krasus1966.liteflow.mybatis.service.LiteflowChainServiceImpl;
import top.krasus1966.liteflow.mybatis.service.LiteflowScriptServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Krasus1966
 * @date 2023/7/9 16:09
 **/
@Service
public class LiteflowStoreService implements IRuleStoreService {

    private final LiteflowChainServiceImpl liteflowChainService;
    private final LiteflowScriptServiceImpl liteflowScriptService;

    public LiteflowStoreService(LiteflowChainServiceImpl liteflowChainService, LiteflowScriptServiceImpl liteflowScriptService) {
        this.liteflowChainService = liteflowChainService;
        this.liteflowScriptService = liteflowScriptService;
    }

    private static Consumer<LiteflowChainPersistent> chainToRule(List<Rule> result) {
        return item -> {
            Rule rule = new Rule();
            rule.setId(item.getId());
            rule.setAppName(item.getApplicationName());
            rule.setRuleKey(item.getChainKey());
            rule.setRuleName(item.getChainName());
            rule.setRuleData(item.getElData());
            result.add(rule);
        };
    }

    private static Consumer<? super LiteflowScriptPersistent> chainToRuleScript(List<RuleScript> result) {
        return item -> {
            RuleScript script = new RuleScript();
            script.setId(item.getId());
            script.setAppName(item.getApplicationName());
            script.setScriptKey(item.getScriptId());
            script.setScriptName(item.getScriptName());
            script.setScriptData(item.getScriptData());
            script.setScriptType(item.getScriptType());
            script.setScriptLanguage(item.getScriptLanguage());
            result.add(script);
        };
    }

    @Override
    public boolean hasRuleKey(String key) {
        return liteflowChainService.lambdaQuery().eq(LiteflowChainPersistent::getChainKey, key).count() > 0;
    }

    @Override
    public boolean hasRuleScriptKey(String key) {
        return liteflowScriptService.lambdaQuery()
                .eq(LiteflowScriptPersistent::getScriptId, key)
                .count() > 0;
    }

    @Override
    public boolean saveRule(Rule rule) {
        LiteflowChainPersistent persistent = new LiteflowChainPersistent(rule);
        Long count = liteflowChainService.lambdaQuery()
                .ne(StrUtil.isNotBlank(persistent.getId()), LiteflowChainPersistent::getId, persistent.getId())
                .eq(LiteflowChainPersistent::getChainKey, persistent.getChainKey())
                .count();
        if (count > 0) {
            throw new BizException("key在系统内已存在");
        }
        return liteflowChainService.saveOrUpdate(persistent);
    }

    @Override
    public boolean saveScript(RuleScript script) {
        LiteflowScriptPersistent persistent = new LiteflowScriptPersistent(script);
        Long count = liteflowScriptService.lambdaQuery()
                .ne(StrUtil.isNotBlank(persistent.getId()), LiteflowScriptPersistent::getId, persistent.getId())
                .eq(LiteflowScriptPersistent::getScriptId, persistent.getScriptId())
                .count();
        if (count > 0) {
            throw new BizException("key在系统内已存在");
        }
        return liteflowScriptService.saveOrUpdate(persistent);
    }

    @Override
    public List<Rule> queryRules(Rule rule) {
        LiteflowChainPersistent persistent = new LiteflowChainPersistent(rule);
        List<LiteflowChainPersistent> list = liteflowChainService.lambdaQuery(persistent).list();
        List<Rule> result = new ArrayList<>();
        list.forEach(chainToRule(result));
        return result;
    }

    @Override
    public List<RuleScript> queryRuleScripts(RuleScript script) {
        LiteflowScriptPersistent persistent = new LiteflowScriptPersistent(script);
        List<LiteflowScriptPersistent> list = liteflowScriptService.lambdaQuery(persistent).list();
        List<RuleScript> result = new ArrayList<>();
        list.forEach(chainToRuleScript(result));
        return result;
    }

    @Override
    public PageResultDTO<Rule> queryPageRules(Rule rule, Integer page, Integer pageSize) {
        LiteflowChainPersistent persistent = new LiteflowChainPersistent(rule);
        PageDTO<LiteflowChainPersistent> pageDTO = new PageDTO<>(page, pageSize);
        PageDTO<LiteflowChainPersistent> page1 = liteflowChainService.lambdaQuery(persistent).page(pageDTO);
        List<Rule> result = new ArrayList<>();
        page1.getRecords().forEach(chainToRule(result));
        return new PageResultDTO<>(result, page1.getTotal());
    }

    @Override
    public PageResultDTO<RuleScript> queryPageRuleScripts(RuleScript script, Integer page, Integer pageSize) {
        LiteflowScriptPersistent persistent = new LiteflowScriptPersistent(script);
        PageDTO<LiteflowScriptPersistent> pageDTO = new PageDTO<>(page, pageSize);
        PageDTO<LiteflowScriptPersistent> page1 = liteflowScriptService.lambdaQuery(persistent).page(pageDTO);
        List<RuleScript> result = new ArrayList<>();
        page1.getRecords().forEach(chainToRuleScript(result));
        return new PageResultDTO<>(result, page1.getTotal());
    }
}
