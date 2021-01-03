package com.troojer.msevent.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MDCInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put("User-Id", (request.getHeader("User-Id") != null) ? request.getHeader("User-Id") : "xxx");
        MDC.put("Request-Id", (request.getHeader("Request-Id") != null) ? request.getHeader("Request-Id") : "xxx");
        MDC.put("Session-Id", (request.getHeader("Session-Id") != null) ? request.getHeader("Session-Id") : "xxx");

        return true;
    }
}
