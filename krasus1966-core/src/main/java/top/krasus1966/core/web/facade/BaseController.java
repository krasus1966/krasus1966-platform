package top.krasus1966.core.web.facade;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Krasus1966
 * {@code {@code @date}} 2023/4/5 00:33
 **/
public abstract class BaseController {
    protected final HttpServletRequest request;
    protected final HttpServletResponse response;

    public BaseController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
