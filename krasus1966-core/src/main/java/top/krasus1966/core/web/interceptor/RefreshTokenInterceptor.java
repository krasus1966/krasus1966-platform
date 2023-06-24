package top.krasus1966.core.web.interceptor;

import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import top.krasus1966.core.base.constant.LoginConstants;
import top.krasus1966.core.web.auth.entity.UserLoginInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * token刷新拦截器-需要在登录拦截前执行
 *
 * @author Krasus1966
 * @date 2022/10/26 20:07
 **/
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;
    private final LoginConstants loginConstants;

    public RefreshTokenInterceptor(LoginConstants loginConstants,
                                   StringRedisTemplate stringRedisTemplate) {
        this.loginConstants = loginConstants;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 从请求头中获取token
        String token = request.getHeader(loginConstants.getHeaderUserToken());
        if (CharSequenceUtil.isBlank(token)) {
            return true;
        }
        // 获取用户信息
        Map<String, String> userMap =
                stringRedisTemplate.<String, String>opsForHash().entries(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token);
        // 判断用户是否存在
        if (userMap.isEmpty()) {
            return true;
        }
        UserLoginInfo info = UserLoginInfo.toInfo(userMap);
        String redisToken =
                stringRedisTemplate.opsForValue().get(top.krasus1966.core.web.constant.LoginConstants.USER_TOKEN + info.getTenantId() + ":" + info.getId());
        // 缓存中登录用户的token和当前请求客户端的token不一致，删除当前请求客户端token关联的用户数据
        if (!Objects.equals(redisToken, token)) {
            // 删除缓存用户信息
            stringRedisTemplate.delete(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token);
            return true;
        }
        // 刷新用户信息
        stringRedisTemplate.expire(top.krasus1966.core.web.constant.LoginConstants.USER_INFO + token,
                loginConstants.getExpireTimeLogin(), TimeUnit.SECONDS);
        stringRedisTemplate.expire(top.krasus1966.core.web.constant.LoginConstants.USER_TOKEN + info.getTenantId() + ":" + info.getId(), loginConstants.getExpireTimeLogin(), TimeUnit.SECONDS);
        return true;
    }
}
