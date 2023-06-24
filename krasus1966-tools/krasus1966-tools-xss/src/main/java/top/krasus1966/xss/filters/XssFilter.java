package top.krasus1966.xss.filters;


import top.krasus1966.xss.wrapper.XssRequestWrapper;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Krasus1966
 * @date 2021/9/26 22:25
 **/
public class XssFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        //传入重写后的Request
        chain.doFilter(new XssRequestWrapper(req), response);
    }
}
