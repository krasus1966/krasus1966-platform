package top.krasus1966.core.handler;

import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.krasus1966.core.anno.auth.LoginUser;
import top.krasus1966.core.constant.PropertiesConstants;
import top.krasus1966.core.entity.auth.UserLoginInfo;
import top.krasus1966.core.util.login.LoginUtils;

/**
 * 自动填充登录用户信息的参数解析器
 *
 * @author Krasus1966
 * @date 2022/11/2 14:42
 **/
public class UserLoginInfoArgumentHandler implements HandlerMethodArgumentResolver {

    private final PropertiesConstants propertiesConstants;

    public UserLoginInfoArgumentHandler(PropertiesConstants propertiesConstants) {
        this.propertiesConstants = propertiesConstants;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) && parameter.getParameterType().equals(UserLoginInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(propertiesConstants.getHeaderUserToken());
        if (CharSequenceUtil.isBlank(token)) {
            return null;
        }
        return LoginUtils.getUserLoginInfo(token);
    }
}
