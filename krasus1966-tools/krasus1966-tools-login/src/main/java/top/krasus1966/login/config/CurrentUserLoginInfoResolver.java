package top.krasus1966.login.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.krasus1966.login.anno.CurrentUserLoginInfo;
import top.krasus1966.login.entity.UserLoginInfo;

/**
 * @author Krasus1966
 * @date 2021/9/27 23:02
 **/
public class CurrentUserLoginInfoResolver implements HandlerMethodArgumentResolver {

    public CurrentUserLoginInfoResolver() {
        System.out.println("CurrentUserMethodArgumentResolver自定义参数解析器初始化...");
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //如果Controller的方法参数类型为User同时还加入了CurrentUser注解，则返回true
        if (parameter.getParameterType().equals(UserLoginInfo.class) &&
                parameter.hasParameterAnnotation(CurrentUserLoginInfo.class)) {
            return true;
        }
        return false;
    }

    /**
     * 当supportsParameter方法返回true时执行此方法
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        System.out.println("参数解析器...");
        //此处直接模拟了一个User对象，实际项目中可能需要从请求头中获取登录用户的令牌然后进行解析，
        //最终封装成User对象返回即可，这样在Controller的方法形参就可以直接引用到User对象了
        UserLoginInfo user = new UserLoginInfo();

        return user;
    }
}
