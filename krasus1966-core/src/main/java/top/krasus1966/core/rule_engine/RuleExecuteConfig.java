package top.krasus1966.core.rule_engine;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author Krasus1966
 * @date 2023/7/9 00:51
 **/
@Configuration
public class RuleExecuteConfig {
    @Bean
    @Order(value = Integer.MIN_VALUE)
    @ConditionalOnMissingBean
    public IRuleExecuteService getDefaultRuleExecuteService(){
        return new NoneRuleExecuteServiceImpl();
    }
}
