package com.troojer.msevent.interceptor;

import com.troojer.msevent.model.CurrentUser;
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
        currentUser.setId(request.getHeader("User-Id"));
        return true;
    }
}
