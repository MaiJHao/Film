package com.film.interceptor;

import com.film.annotation.AdminLoginRequired;
import com.film.annotation.LoginRequired;
import com.film.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AdminLoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AdminLoginRequired adminLoginRequired = method.getAnnotation(AdminLoginRequired.class);
            if (adminLoginRequired != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/admin/login");
                return false;
            }
        }
        return true;
    }
}
