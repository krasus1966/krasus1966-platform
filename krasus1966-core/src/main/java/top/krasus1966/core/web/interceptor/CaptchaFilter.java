package top.krasus1966.core.web.interceptor;

import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.krasus1966.core.cache.redis_util.CacheUtil;
import top.krasus1966.core.web.constant.LoginConstants;
import top.krasus1966.core.web.exception.CaptchaException;
import top.krasus1966.core.web.util.servlet.ServletUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Krasus1966
 * @date 2022/3/4 11:26
 **/
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String captcha = request.getHeader("imageToken");
        if (null != captcha && !"".equals(captcha) && HttpMethod.POST.name().equals(request.getMethod())) {
            try {
                validate(request);
            } catch (CaptchaException e) {
                ServletUtils.setCaptchaErrorResponse(response, e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) {
        String captchaToken = request.getParameter("token");
        String code = request.getParameter("code");
        if (CharSequenceUtil.isBlank(code) || CharSequenceUtil.isBlank(captchaToken)) {
            throw new CaptchaException("验证码不能为空");
        }
        String cacheCode = CacheUtil.hget(LoginConstants.CAPTCHA_KEY, captchaToken);
        if (CharSequenceUtil.isBlank(cacheCode)) {
            throw new CaptchaException("验证码已过期");
        }
        if (!code.equals(cacheCode)) {
            throw new CaptchaException("验证码不正确");
        }
        // 使用后删除图片缓存
        CacheUtil.hdel(LoginConstants.CAPTCHA_KEY, captchaToken);
    }
}
