package top.krasus1966.liteflow.service;

import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.script.ScriptBeanManager;
import com.yomahub.liteflow.script.ScriptExecutorFactory;
import org.springframework.stereotype.Service;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.liteflow.entity.LiteflowChainPersistent;
import top.krasus1966.liteflow.mybatis.service.LiteflowChainServiceImpl;

/**
 * @author Krasus1966
 * @date 2023/7/8 20:44
 **/
@Service
public class LiteflowExecuteService implements IRuleExecuteService {

    private final FlowExecutor flowExecutor;
    private final LiteflowChainServiceImpl liteflowChainService;

    public LiteflowExecuteService(FlowExecutor flowExecutor, LiteflowChainServiceImpl liteflowChainService) {
        this.flowExecutor = flowExecutor;
        this.liteflowChainService = liteflowChainService;
    }

    @Override
    public boolean hasRule(String key) {
        return liteflowChainService.lambdaQuery().eq(LiteflowChainPersistent::getChainKey,key).count() > 0;
    }

    /**
     * 无返回值执行流程
     *
     * @param key     流程key
     * @param param   流程参数
     * @param context 上下文
     * @return void
     * @throws Exception
     * @method execute
     * @author krasus1966
     * @date 2023/7/8 21:51
     * @description 无返回值执行流程
     */
    @Override
    public void execute(String key, Object param, Object... context) throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp(key, param, context);
        if (!response.isSuccess()) {
            // 有全局异常处理，走全局异常处理
            throw response.getCause();
        }
    }

    /**
     * 带返回值执行流程
     *
     * @param key         流程key
     * @param param       流程参数
     * @param returnClazz 返回值类型，这个返回值会从上下文中获取，所以返回值一定在上下文中
     * @param context     上下文
     * @return T
     * @throws Exception
     * @method execute
     * @author krasus1966
     * @date 2023/7/8 21:51
     * @description 带返回值执行流程
     */
    @Override
    public <T> T execute(String key, Object param, Class<T> returnClazz, Object... context) throws Exception {
        LiteflowResponse response = flowExecutor.execute2Resp(key, param, context);
        if (!response.isSuccess()) {
            // 有全局异常处理，走全局异常处理
            throw response.getCause();
        }
        return response.getContextBean(returnClazz);
    }

    /**
     * 添加自定义Java对象，添加后可全局使用对象内的方法
     * 需要Java对象标注@ScriptBean
     * 不可以使用静态方法
     *
     * @param key  名称
     * @param bean 注入对象
     * @return void
     * @throws
     * @method addScriptBean
     * @author krasus1966
     * @date 2023/7/8 22:27
     * @description 添加自定义Java对象，添加后可全局使用对象内的方法
     */
    @Override
    public void addScriptBean(String key, Object bean) {
        ScriptBeanManager.addScriptBean(key, bean);
    }

    @Override
    public boolean validRule(String elData) {
        return LiteFlowChainELBuilder.validate(elData);
    }

    /**
     * 刷新所有编排和脚本
     *
     * @return void
     * @method reloadRule
     * @author krasus1966
     * @date 2023/7/8 22:06
     * @description 刷新所有编排和脚本
     */
    @Override
    public void reloadRule() {
        flowExecutor.reloadRule();
    }

    /**
     * 刷新特定编排
     *
     * @param key    key
     * @param elData 编排内容
     * @return void
     * @throws
     * @method reloadChain
     * @author krasus1966
     * @date 2023/7/8 22:38
     * @description 刷新特定编排
     */
    @Override
    public void reloadRule(String key, String elData) {
        LiteFlowChainELBuilder
                .createChain()
                .setChainId(key)
                .setEL(elData)
                .build();
    }

    /**
     * 重新刷新脚本
     *
     * @param language 语言
     * @param key      key
     * @param script   脚本内容
     * @return void
     * @throws
     * @method reloadScript
     * @author krasus1966
     * @date 2023/7/8 22:38
     * @description 重新刷新脚本
     */
    @Override
    public void reloadScript(String language, String key, String script) {
        ScriptExecutorFactory.loadInstance().getScriptExecutor(language).load(key, script);
    }
}
