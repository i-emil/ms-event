package com.troojer.msevent.interceptor;

import com.troojer.msevent.model.CurrentUser;
import com.troojer.msevent.model.exception.AuthenticationException;
import com.troojer.msevent.util.ToolUtil;
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
        if (userId == null || userId.isBlank()) throw new AuthenticationException(ToolUtil.getMessage("auth.error"));
        currentUser.setId(userId);
        return true;
    }
}
