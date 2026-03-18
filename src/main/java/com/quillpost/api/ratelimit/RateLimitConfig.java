package com.quillpost.api.ratelimit;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RateLimitConfig implements WebMvcConfigurer {

    private final ApiRateLimitInterceptor interceptor;
    private final TokenRateLimitInterceptor tokenInterceptor;

    public RateLimitConfig(ApiRateLimitInterceptor interceptor, TokenRateLimitInterceptor tokenInterceptor) {
        this.interceptor = interceptor;
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/api/**");
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/api/**");
    }
}
