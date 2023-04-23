package top.krasus1966.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import top.krasus1966.core.handler.InternationalLocaleResolve;

/**
 * @author Krasus1966
 * {@code @date} 2023/4/3 23:24
 **/
@Configuration
public class InternationalLocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new InternationalLocaleResolve();
    }
}
