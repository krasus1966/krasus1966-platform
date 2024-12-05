package top.krasus1966.common.web.handler;

import cn.hutool.core.text.CharSequenceUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import top.krasus1966.common.core.cache.util.CacheUtil;
import top.krasus1966.common.core.constant.LoginCacheConstant;
import top.krasus1966.common.core.constant.LoginConstants;
import top.krasus1966.common.core.entity.UserLoginInfo;

import java.util.Map;
import java.util.Objects;

/**
 * token刷新拦截器-需要在登录拦截前执行
 *
 * @author Krasus1966
 * @date 2022/10/26 20:07
 **/
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private final LoginConstants loginConstants;

    public RefreshTokenInterceptor(LoginConstants loginConstants) {
        this.loginConstants = loginConstants;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = request.getHeader(loginConstants.getHeaderUserToken());
        if (CharSequenceUtil.isBlank(token)) {
            return true;
        }
        // 获取用户信息
        Map<String, String> userMap = CacheUtil.hget(LoginCacheConstant.USER_INFO + token);
        // 判断用户是否存在
        if (userMap.isEmpty()) {
            return true;
        }
        UserLoginInfo info = UserLoginInfo.toInfo(userMap);
        String redisToken = CacheUtil.get(LoginCacheConstant.USER_TOKEN + info.getTenantId() + ":" + info.getId());
        // 缓存中登录用户的token和当前请求客户端的token不一致，删除当前请求客户端token关联的用户数据
        if (!Objects.equals(redisToken, token)) {
            // 删除缓存用户信息
            CacheUtil.del(LoginCacheConstant.USER_INFO + token);
            return true;
        }
        // 刷新用户信息
        CacheUtil.expire(LoginCacheConstant.USER_INFO + token, loginConstants.getExpireTimeLogin());
        CacheUtil.expire(LoginCacheConstant.USER_TOKEN + info.getTenantId() + ":" + info.getId(),
                loginConstants.getExpireTimeLogin());
        return true;
    }
}
