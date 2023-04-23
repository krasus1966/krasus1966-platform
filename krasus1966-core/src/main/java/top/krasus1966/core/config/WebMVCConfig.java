package top.krasus1966.core.config;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.krasus1966.core.constant.PropertiesConstants;
import top.krasus1966.core.interceptor.LoginInterceptor;
import top.krasus1966.core.util.spring.SpringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置拦截器-改为每个服务自己实现
 *
 * @author Krasus1966
 * @date 2021/5/5 13:54
 **/
//@Configuration
//@EnableConfigurationProperties(PropertiesConstants.class)
public class WebMVCConfig implements WebMvcConfigurer {

    private final StringRedisTemplate stringRedisTemplate;

    private final PropertiesConstants propertiesConstants;

    public WebMVCConfig(StringRedisTemplate stringRedisTemplate,
                        PropertiesConstants propertiesConstants) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.propertiesConstants = propertiesConstants;
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
        registry.addInterceptor(new LoginInterceptor(propertiesConstants, stringRedisTemplate)).excludePathPatterns(irs);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*")
                .allowedOrigins("http://www.krasus1966.top", "http://127.0.0.1:8889")
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
            registry.addResourceHandler("/service-worker.js").addResourceLocations("classpath" +
                    ":/META-INF/service-worker.js");
            // 解决 SWAGGER 404报错
            registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META" +
                    "-INF/resources/");
            registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF" +
                    "/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF" +
                    "/resources/webjars/");
        }
    }
}
