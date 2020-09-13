package com.troojer.mstag.configuration;

import com.troojer.mstag.interceptor.MDCInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LocaleChangeInterceptor localeChangeInterceptor;
    private final MDCInterceptor mdcInterceptor;

    public WebMvcConfiguration(LocaleChangeInterceptor localeChangeInterceptor, MDCInterceptor mdcInterceptor) {
        this.localeChangeInterceptor = localeChangeInterceptor;
        this.mdcInterceptor = mdcInterceptor;
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
        registry.addInterceptor(localeChangeInterceptor).order(1);
        registry.addInterceptor(mdcInterceptor).order(10);
    }

}
