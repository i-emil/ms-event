package com.troojer.mstag.interceptor;

import com.troojer.mstag.model.CurrentUser;
import com.troojer.mstag.model.exception.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserHandlerInterceptor extends HandlerInterceptorAdapter {

    private final CurrentUser currentUser;

    public UserHandlerInterceptor(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader("User-Id");
        if (userId == null || userId.isBlank()) throw new AuthenticationException("default.auth.unauthorized");
        currentUser.setId(userId);
        return true;
    }
}
