package top.krasus1966.idempotent.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.krasus1966.idempotent.aspect.RepeatSubmitAspect;

/**
 * @author Krasus1966
 * @date 2021/10/17 21:24
 **/
@Configuration
@ConditionalOnProperty(name = "krasus1966.idempotent.enabled", havingValue = "true", matchIfMissing = false)
public class IdempotentConfig {

    @Bean
    public RepeatSubmitAspect repeatSubmitAspect(){
        return new RepeatSubmitAspect();
    }
}
