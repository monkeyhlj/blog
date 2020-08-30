package com.monkey.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//做登录拦截（过滤器）
public class LoginInterceptor extends HandlerInterceptorAdapter { //拦截器

    //预处理
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if(request.getSession().getAttribute("user") == null){
            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }
}
