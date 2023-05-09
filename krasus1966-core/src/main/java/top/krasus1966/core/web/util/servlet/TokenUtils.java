package top.krasus1966.core.web.util.servlet;


import cn.hutool.core.text.CharSequenceUtil;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.base.enums.ResultsEnum;
import top.krasus1966.core.cache.redis_util.CacheUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Krasus1966
 * @date 2020/10/29 22:44
 **/
public class TokenUtils {


    private TokenUtils() {
    }

    //将Token存到cookie中
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue, Integer maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            Cookie cookie = new Cookie(cookieName, cookieValue);
            cookie.setMaxAge(maxAge);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void setHeader(HttpServletResponse response, String headerName, String token) {
        try {
            response.setHeader(headerName, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifyUserToken(String id, String token, String redisKeyPrefix) {
        if (CharSequenceUtil.isNotBlank(id) && CharSequenceUtil.isNotBlank(token)) {
            String redisToken = CacheUtil.get(redisKeyPrefix + ":" + id);
            if (CharSequenceUtil.isBlank(redisToken)) {
                R.error(ResultsEnum.SESSION_INVALID);
                return false;
            } else {
                if (!redisToken.equals(token)) {
                    R.error(ResultsEnum.UN_LOGIN);
                    return false;
                }
                return true;
            }
        } else {
            R.error(ResultsEnum.UN_LOGIN);
            return false;
        }
    }
}
