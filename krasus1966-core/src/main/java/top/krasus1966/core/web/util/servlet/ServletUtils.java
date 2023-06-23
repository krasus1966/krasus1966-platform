package top.krasus1966.core.web.util.servlet;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.krasus1966.core.base.enums.ResultsEnum;
import top.krasus1966.core.json.util.JsonUtils;
import top.krasus1966.core.web.entity.R;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Krasus1966
 * @date 2021/11/24 23:30
 **/
public class ServletUtils {

    /**
     * 获取HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    /**
     * 获取HttpServletResponse
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }


    public static void setCaptchaErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        PrintWriter writer = response.getWriter();
        writer.print(JsonUtils.objectToJson(R.parse(ResultsEnum.CAPTCHA_ERROR.getCode(), message,
                null)));
        writer.close();
        response.flushBuffer();
    }

    /**
     * 未登录返回
     *
     * @param response
     * @return void
     * @method setNoLoginResponse
     * @author krasus1966
     * @date 2022/3/4 11:27
     * @description 未登录返回
     */
    public static void setNoLoginResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        writer.print(JsonUtils.objectToJson(R.noLogin()));
        writer.close();
        response.flushBuffer();
    }

    /**
     * 其他端登录返回
     *
     * @param response
     * @return void
     * @method setRepeatLoginResponse
     * @author krasus1966
     * @date 2022/3/4 11:27
     * @description 未登录返回
     */
    public static void setRepeatLoginResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        writer.print(JsonUtils.objectToJson(R.repeatLogin()));
        writer.close();
        response.flushBuffer();
    }

    /**
     * 无权限返回
     *
     * @param response
     * @return void
     * @method setNoPermissionResponse
     * @author krasus1966
     * @date 2022/3/4 11:28
     * @description 无权限返回
     */
    public static void setNoPermissionResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        writer.print(JsonUtils.objectToJson(R.noPermission()));
        writer.close();
        response.flushBuffer();
    }

    /**
     * 签名错误
     *
     * @param response
     * @return void
     * @method setSignErrResponse
     * @author krasus1966
     * @date 2022/3/4 11:28
     * @description 无权限返回
     */
    public static void setSignErrResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        writer.print(JsonUtils.objectToJson(R.signErr()));
        writer.close();
        response.flushBuffer();
    }

    /**
     * 签名错误
     *
     * @param response
     * @return void
     * @method setSignErrResponse
     * @author krasus1966
     * @date 2022/3/4 11:28
     * @description 无权限返回
     */
    public static void setSignRepeatResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        writer.print(JsonUtils.objectToJson(R.signRepeat()));
        writer.close();
        response.flushBuffer();
    }
}
