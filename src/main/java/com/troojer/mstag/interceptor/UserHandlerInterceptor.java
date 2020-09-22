package com.troojer.mstag.interceptor;

import com.troojer.mstag.model.CurrentUser;
import com.troojer.mstag.model.exception.AuthenticationException;
import com.troojer.mstag.util.ToolUtil;
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
        if (request.getHeader("User-Id") == null) throw new AuthenticationException(ToolUtil.getMessage("auth.error"));
        currentUser.setId(request.getHeader("User-Id"));
        return true;
    }
}
