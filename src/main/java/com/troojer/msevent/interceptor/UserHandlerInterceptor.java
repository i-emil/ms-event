package com.troojer.msevent.interceptor;

import com.troojer.msevent.model.CurrentUser;
import com.troojer.msevent.model.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class UserHandlerInterceptor implements HandlerInterceptor {

    private final CurrentUser currentUser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader("User-Id");
        if (userId == null || userId.isBlank()) throw new AuthenticationException("default.auth.unauthorized");
        currentUser.setId(userId);
        return true;
    }
}
