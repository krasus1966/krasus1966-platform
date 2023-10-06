package top.krasus1966.core.web.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.krasus1966.core.base.constant.LoginConstants;
import top.krasus1966.core.spring.util.SpringUtil;
import top.krasus1966.core.web.interceptor.LoginInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置拦截器-改为每个服务自己实现
 *
 * @author Krasus1966
 * @date 2021/5/5 13:54
 **/
@Configuration
@EnableConfigurationProperties(LoginConstants.class)
public class WebMVCConfig implements WebMvcConfigurer {

    private final StringRedisTemplate stringRedisTemplate;

    private final LoginConstants loginConstants;

    public WebMVCConfig(StringRedisTemplate stringRedisTemplate,
                        LoginConstants loginConstants) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.loginConstants = loginConstants;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String env = SpringUtil.getProperty("spring.profiles.active");
        List<String> irs = new ArrayList<String>();
        if (!"prod".equals(env)) {
            // 开放knife4j
            irs.add("/doc.html");
            irs.add("/swagger-resources/**");
            irs.add("/webjars/**");
            irs.add("/service-worker.js");
        }
        registry.addInterceptor(new LoginInterceptor(loginConstants, stringRedisTemplate)).excludePathPatterns(irs);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*")
//                .allowedOrigins("http://www.krasus1966.top", "http://127.0.0.1:8889")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3000);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String env = SpringUtil.getProperty("spring.profiles.active");
        if (!"prod".equals(env)) {
            registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
            registry.addResourceHandler("/service-worker.js").addResourceLocations("classpath:/META-INF/service-worker.js");
            // 解决 SWAGGER 404报错
            registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}
