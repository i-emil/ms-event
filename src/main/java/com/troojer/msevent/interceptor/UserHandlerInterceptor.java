package com.troojer.msevent.interceptor;

import com.troojer.msevent.client.UserPlanClient;
import com.troojer.msevent.model.CurrentUser;
import com.troojer.msevent.model.exception.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserHandlerInterceptor implements HandlerInterceptor {

    private final CurrentUser currentUser;

    private final UserPlanClient userPlanClient;

    public UserHandlerInterceptor(CurrentUser currentUser, UserPlanClient userPlanClient) {
        this.currentUser = currentUser;
        this.userPlanClient = userPlanClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader("User-Id");
        if (userId == null || userId.isBlank()) throw new AuthenticationException("default.auth.unauthorized");
        currentUser.setId(userId);
        currentUser.setUserPlan(userPlanClient.getUserPlan());
        return true;
    }
}
