package top.krasus1966.common.web.handler;

import cn.hutool.core.text.CharSequenceUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.krasus1966.common.core.cache.CacheFactory;
import top.krasus1966.common.core.constant.LoginCacheConstant;
import top.krasus1966.common.web.exception.CaptchaException;
import top.krasus1966.common.web.util.ServletUtils;

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
        String cacheCode = CacheFactory.getCache().hget(LoginCacheConstant.CAPTCHA_KEY, captchaToken);
        if (CharSequenceUtil.isBlank(cacheCode)) {
            throw new CaptchaException("验证码已过期");
        }
        if (!code.equals(cacheCode)) {
            throw new CaptchaException("验证码不正确");
        }
        // 使用后删除图片缓存
        CacheFactory.getCache().hdel(LoginCacheConstant.CAPTCHA_KEY, captchaToken);
    }
}
