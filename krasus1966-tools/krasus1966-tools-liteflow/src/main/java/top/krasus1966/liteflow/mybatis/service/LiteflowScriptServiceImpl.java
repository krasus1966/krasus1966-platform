package top.krasus1966.liteflow.mybatis.service;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import top.krasus1966.core.base.exception.BizException;
import top.krasus1966.core.db.mybatis_plus.service.AbstractMybatisBaseService;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.liteflow.entity.persistent.LiteflowScriptPersistent;
import top.krasus1966.liteflow.mybatis.mapper.LiteflowScriptMapper;

/**
 * @author Krasus1966
 * @date 2023/7/8 22:48
 **/
@Service
public class LiteflowScriptServiceImpl extends AbstractMybatisBaseService<LiteflowScriptMapper, LiteflowScriptPersistent> {
    private final IRuleExecuteService ruleExecuteService;

    public LiteflowScriptServiceImpl(IRuleExecuteService ruleExecuteService) {
        this.ruleExecuteService = ruleExecuteService;
    }


    @Override
    public void beforeSave(LiteflowScriptPersistent persistent) {
        Long count = lambdaQuery()
                .ne(StrUtil.isNotBlank(persistent.getId()), LiteflowScriptPersistent::getId,persistent.getId())
                .eq(LiteflowScriptPersistent::getScriptId,persistent.getScriptId())
                .count();
        if (count > 0) {
            throw new BizException("key在系统内已存在");
        }
    }

    @Override
    public void beforeUpdate(LiteflowScriptPersistent persistent) {
        this.beforeSave(persistent);
    }

    @Override
    public void afterSave(LiteflowScriptPersistent persistent) {
        // 脚本热刷新不生效
        // ruleExecuteService.reloadScript(persistent.getScriptLanguage(),persistent.getScriptId(),persistent.getScriptData());
        ruleExecuteService.reloadRule();
    }

    @Override
    public void afterUpdate(LiteflowScriptPersistent persistent) {
        this.afterSave(persistent);
    }
}
