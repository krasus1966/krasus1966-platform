package top.krasus1966.liteflow.init;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.liteflow.script.StringUtil;

/**
 * @author Krasus1966
 * @date 2023/5/3 16:27
 **/
@ComponentScan("top.krasus1966.core")
@MapperScan("top.krasus1966.liteflow")
@Component
public class AppInit implements ApplicationRunner {
    private final IRuleExecuteService ruleExecuteService;

    public AppInit(IRuleExecuteService ruleExecuteService) {
        this.ruleExecuteService = ruleExecuteService;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ruleExecuteService.addScriptBean("StringUtil",new StringUtil());
    }


}
