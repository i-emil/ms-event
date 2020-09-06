package com.troojer.msevent.configuration;

import com.troojer.msevent.interceptor.MDCInterceptor;
import com.troojer.msevent.interceptor.UserHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final UserHandlerInterceptor userHandlerInterceptor;
    private final LocaleChangeInterceptor localeChangeInterceptor;
    private final MDCInterceptor mdcInterceptor;

    public WebMvcConfiguration(UserHandlerInterceptor userHandlerInterceptor, LocaleChangeInterceptor localeChangeInterceptor, MDCInterceptor mdcInterceptor) {
        this.userHandlerInterceptor = userHandlerInterceptor;
        this.localeChangeInterceptor = localeChangeInterceptor;
        this.mdcInterceptor = mdcInterceptor;
    }

    // Static Resource Config
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
        registry.addInterceptor(localeChangeInterceptor).order(1);
        registry.addInterceptor(mdcInterceptor).order(10);
    }

}
