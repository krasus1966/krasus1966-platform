//package top.krasus1966.login.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
///**
// * @author Krasus1966
// * @date 2021/9/27 23:03
// **/
//@Configuration
//public class ArgumentResolverConfiguration implements WebMvcConfigurer {
//
//    public CurrentUserLoginInfoResolver getCurrentUserLoginInfoResolver() {
//        return new CurrentUserLoginInfoResolver();
//    }
//
//    /**
//     * 注册自定义参数解析器
//     *
//     * @param resolvers
//     */
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(getCurrentUserLoginInfoResolver());
//    }
//}
