package com.appcnd.find.api.filter;

import com.appcnd.find.api.util.DesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author nihao 2018/11/23
 */
public class AuthFilter implements Filter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getHeader("token");
        Long id = null;
        if (token != null) {
            try {
                String id_ = DesUtil.decrypt(token);
                id = Long.parseLong(id_);
            } catch (Exception e) {
                LOGGER.error("解析token:{} 错误", token, e);
            }
        }
        if (id != null) {
            request.setAttribute("uid", id);
            filterChain.doFilter(request, response);
        }
        else {
            response.setCharacterEncoding("UTF-8");
            response.sendError(403, "未登录");
        }
    }

    @Override
    public void destroy() {

    }
}
