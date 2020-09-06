package com.troojer.msevent.interceptor;

import com.troojer.msevent.util.ToolUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MDCInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put("userId", request.getHeader("User-Id"));
        MDC.put("requestId", ToolUtil.getRandomCode(7));
        return true;
    }
}
