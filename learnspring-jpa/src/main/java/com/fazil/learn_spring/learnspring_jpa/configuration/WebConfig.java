package com.fazil.learn_spring.learnspring_jpa.configuration;

import com.fazil.learn_spring.learnspring_jpa.interceptor.ExecutionTimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    ExecutionTimeInterceptor executionTimeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(executionTimeInterceptor).addPathPatterns("/**");
    }
}
