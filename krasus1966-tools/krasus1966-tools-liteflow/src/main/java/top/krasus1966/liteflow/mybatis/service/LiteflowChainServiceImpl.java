package top.krasus1966.liteflow.mybatis.service;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.liteflow.entity.persistent.LiteflowChainPersistent;
import top.krasus1966.liteflow.mybatis.mapper.LiteflowChainMapper;

/**
 * @author Krasus1966
 * @date 2023/7/8 22:48
 **/
@Service
public class LiteflowChainServiceImpl extends AbstractMybatisBaseService<LiteflowChainMapper, LiteflowChainPersistent> {

    private final IRuleExecuteService ruleExecuteService;

    public LiteflowChainServiceImpl(IRuleExecuteService ruleExecuteService) {
        this.ruleExecuteService = ruleExecuteService;
    }

    @Override
    public void beforeSave(LiteflowChainPersistent persistent) {
        Long count = lambdaQuery()
                .ne(StrUtil.isNotBlank(persistent.getId()),LiteflowChainPersistent::getId,persistent.getId())
                .eq(LiteflowChainPersistent::getChainKey,persistent.getChainKey())
                .count();
        if (count > 0) {
            throw new BizException("key在系统内已存在");
        }
        if (!ruleExecuteService.validRule(persistent.getElData())) {
            throw new BizException("表达式错误");
        }
    }

    @Override
    public void afterSave(LiteflowChainPersistent persistent) {
        // ruleExecuteService.reloadRule(persistent.getChainKey(),persistent.getElData());
        ruleExecuteService.reloadRule();
    }

    @Override
    public void beforeUpdate(LiteflowChainPersistent persistent) {
        this.beforeSave(persistent);
    }

    @Override
    public void afterUpdate(LiteflowChainPersistent persistent) {
        this.afterSave(persistent);
    }
}
