package com.jluslda.edu.filter;

import com.jluslda.edu.model.User;
import com.jluslda.edu.service.UserService;
import com.jluslda.edu.util.Common;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebFilter("/*")
public class UserFilter implements Filter {

    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        var context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        assert context != null;
        userService = context.getBean(UserService.class);
    }

    private boolean isBeforeSigninPage(String url) {
        // 登录前才能访问的地址
        var arr = Arrays.asList("/signin", "/signup", "/forget");
        return arr.contains(url);
    }

    private boolean isAfterSigninPage(String url) {
        // 登录后才能访问的地址
        if (url.startsWith("/account"))
            return true;
        var arr = Arrays.asList("/community/create", "");
        return arr.contains(url);
    }

    private boolean isAfterSigninAPI(String url) {
        // 登录后才能访问的API
        if (url.startsWith("/api/account/"))
            return true;
        var arr = Arrays.asList("/api/article/create", "/api/community/create", "/api/document/create" ,
                "/api/video/create", "/api/community/deleteTalk", "/api/document/deleteDocument","/api/video/deleteVideo" ,
                "/api/article/deleteArticle", "/api/comment/add", "/api/video/openLive", "/api/video/closeLive", "/api/video/getLive");
        return arr.contains(url);
    }

    private boolean isAdminPage(String url) {
        // 后台地址
        return url.startsWith("/admin");
    }

    private User autoLogin(HttpServletRequest request) {
        // 处理自动登录
        var cookies = request.getCookies();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null && cookies != null) {
            String email = null, password = null;
            for (var t : cookies) {
                if (t.getName().equals("email"))
                    email = t.getValue();
                if (t.getName().equals("password"))
                    password = t.getValue();
            }
            if (email != null && password != null) {
                User tmp = userService.getUserByEmail(email);
                if (tmp.getPassword().equals(password)) {
                    user = tmp;
                    request.getSession().setAttribute("user", user);
                    userService.updateTime(user.getId());
                }
            }
        }
        return user;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;
        var url = httpRequest.getRequestURI();
        var user = autoLogin(httpRequest);

        // 处理页面访问权限
        if (user == null && isAfterSigninPage(url)) {
            httpResponse.sendRedirect("/signin");
        } else if (user != null && isBeforeSigninPage(url)) {
            httpResponse.sendRedirect("/");
        } else if ((user == null || user.getExtra("isAdmin") == null || !((boolean) user.getExtra("isAdmin"))) && isAdminPage(url)) {
            httpResponse.sendRedirect("/");
        } else if (user == null && isAfterSigninAPI(url)) {
            Common.setResponse(Common.standardResp(1,"请先登录",null), httpResponse);
        } else {
            httpRequest.getSession().setAttribute("isSignin", user != null);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

