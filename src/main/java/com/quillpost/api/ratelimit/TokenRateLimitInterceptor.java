package com.quillpost.api.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TokenRateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, AtomicInteger> tokenCounts = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            return true;
        }
        String token = auth.substring(7);
        int used = tokenCounts.computeIfAbsent(token, k -> new AtomicInteger()).incrementAndGet();
        response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, 1000 - used)));
        return used <= 1000;
    }
}
