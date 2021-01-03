package com.troojer.msevent.configuration;

import com.troojer.msevent.interceptor.MDCInterceptor;
import com.troojer.msevent.interceptor.UserHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final UserHandlerInterceptor userHandlerInterceptor;
    private final MDCInterceptor mdcInterceptor;

    public WebMvcConfiguration(UserHandlerInterceptor userHandlerInterceptor, MDCInterceptor mdcInterceptor) {
        this.userHandlerInterceptor = userHandlerInterceptor;
        this.mdcInterceptor = mdcInterceptor;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userHandlerInterceptor).order(10);
        registry.addInterceptor(mdcInterceptor).order(2);
    }

}
