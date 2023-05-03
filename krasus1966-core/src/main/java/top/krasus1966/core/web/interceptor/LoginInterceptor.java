package top.krasus1966.core.web.interceptor;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import top.krasus1966.core.web.auth.anno.Auth;
import top.krasus1966.core.base.constant.LoginConstants;
import top.krasus1966.core.web.auth.entity.UserLoginInfo;
import top.krasus1966.core.web.util.login.LoginUtils;
import top.krasus1966.core.cache.redis_util.CacheUtil;
import top.krasus1966.core.web.util.servlet.ServletUtils;
import top.krasus1966.core.crypto.sign.SignUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 登录拦截器
 *
 * @author Krasus1966
 * @date 2021/11/15 21:29
 **/
public class LoginInterceptor implements HandlerInterceptor {

    private final LoginConstants loginConstants;
    private final StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(LoginConstants loginConstants,
                            StringRedisTemplate stringRedisTemplate) {
        this.loginConstants = loginConstants;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Method method = handlerMethod.getMethod();

            Auth auth = method.getDeclaredAnnotation(Auth.class);
            boolean login = auth != null && auth.login();
            boolean sign = auth != null && auth.sign();
            // 有Auth注解，判断login是否为false，为false不需要登录
            if (login) {
                UserLoginInfo info = LoginUtils.getUserLoginInfo();
                if (null == info) {
                    ServletUtils.setNoLoginResponse(response);
                    return false;
                }
                String redisToken =
                        CacheUtil.get(top.krasus1966.core.web.constant.LoginConstants.USER_TOKEN + info.getTenantId() + ":" + info.getId());
                // 当前用户登录缓存token和缓存用户信息中的token不一致，应删除缓存用户信息
                if (!redisToken.equals(info.getToken())) {
                    CacheUtil.del(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + info.getToken());
                    ServletUtils.setRepeatLoginResponse(response);
                    return false;
                }
            }
            // 有Auth注解，判断sign是否为false，为false不需要签名验证
            if (sign) {
                // 判断签名是否正确
                int signResult = SignUtils.sign(loginConstants);
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
