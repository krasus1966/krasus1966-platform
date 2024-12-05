package top.krasus1966.common.web.handler;


import cn.hutool.core.text.CharSequenceUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import top.krasus1966.common.core.cache.util.CacheUtil;
import top.krasus1966.common.core.constant.LoginCacheConstant;
import top.krasus1966.common.core.constant.LoginConstants;
import top.krasus1966.common.core.crypto.util.SignUtils;
import top.krasus1966.common.core.entity.UserLoginInfo;
import top.krasus1966.common.core.util.AbstractLoginUtil;
import top.krasus1966.common.core.util.SpringUtil;
import top.krasus1966.common.web.anno.Auth;
import top.krasus1966.common.web.util.ServletUtils;

import java.lang.reflect.Method;

/**
 * 登录拦截器
 *
 * @author Krasus1966
 * @date 2021/11/15 21:29
 **/
public class LoginInterceptor implements HandlerInterceptor {

    private final LoginConstants loginConstants;

    public LoginInterceptor(LoginConstants loginConstants) {
        this.loginConstants = loginConstants;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        AbstractLoginUtil loginUtil = SpringUtil.getBean(AbstractLoginUtil.class);
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Method method = handlerMethod.getMethod();

            Auth auth = method.getDeclaredAnnotation(Auth.class);
            boolean login = auth != null && auth.login();
            boolean sign = auth != null && auth.sign();
            // 有Auth注解，判断login是否为false，为false不需要登录
            if (login) {
                UserLoginInfo info = loginUtil.getUserLoginInfo();
                if (null == info) {
                    ServletUtils.setNoLoginResponse(response);
                    return false;
                }
                String redisToken =
                        CacheUtil.get(LoginCacheConstant.USER_TOKEN + info.getTenantId() + ":" + info.getId());
                // 当前用户登录缓存token和缓存用户信息中的token不一致，应删除缓存用户信息
                if (!redisToken.equals(info.getToken())) {
                    CacheUtil.del(LoginCacheConstant.USER_INFO + info.getToken());
                    ServletUtils.setRepeatLoginResponse(response);
                    return false;
                }
            }
            // 有Auth注解，判断sign是否为false，为false不需要签名验证
            if (sign) {
                String webSign =
                        CharSequenceUtil.trim(request.getHeader(loginConstants.getHeaderSignName()));
                String random =
                        CharSequenceUtil.trim(request.getHeader(loginConstants.getHeaderSignRandomName()));
                String token =
                        CharSequenceUtil.trim(request.getHeader(loginConstants.getHeaderUserToken()));
                // 判断签名是否正确
                int signResult = SignUtils.sign(loginConstants, webSign, random, token, request.getParameterMap());
                if (0 == signResult) {
                    ServletUtils.setSignErrResponse(response);
                    return false;
                }
                if (2 == signResult) {
                    ServletUtils.setSignRepeatResponse(response);
                    return false;
                }
            }
        }
        // 静态资源不拦截
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        return true;
    }
}
