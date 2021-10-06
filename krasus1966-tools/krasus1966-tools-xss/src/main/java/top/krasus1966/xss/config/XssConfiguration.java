package top.krasus1966.xss.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.krasus1966.xss.filters.XssFilter;

/**
 * @author Krasus1966
 * @date 2021/9/26 22:28
 **/
@Configuration
public class XssConfiguration {
    /**
     * 配置跨站攻击过滤器
     */
    @Bean
    public FilterRegistrationBean<XssFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssFilter> filterRegistration =
                new FilterRegistrationBean<>(new XssFilter());
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }
}
