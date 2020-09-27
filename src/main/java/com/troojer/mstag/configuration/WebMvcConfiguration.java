package com.troojer.mstag.configuration;

import com.troojer.mstag.interceptor.MDCInterceptor;
import com.troojer.mstag.interceptor.UserHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MDCInterceptor mdcInterceptor;
    private final UserHandlerInterceptor userHandlerInterceptor;

    public WebMvcConfiguration(MDCInterceptor mdcInterceptor, UserHandlerInterceptor userHandlerInterceptor) {
        this.mdcInterceptor = mdcInterceptor;
        this.userHandlerInterceptor = userHandlerInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userHandlerInterceptor).order(2);
        registry.addInterceptor(mdcInterceptor).order(10);
    }

}
