package com.quillpost.api.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ApiRateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!request.getRequestURI().startsWith("/api/")) {
            return true;
        }
        String key = request.getRemoteAddr() + ":" + request.getRequestURI();
        Window window = windows.computeIfAbsent(key, k -> new Window());
        int count = window.increment();
        if (count > 120) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader("Retry-After", "60");
            return false;
        }
        return true;
    }

    private static final class Window {
        private final AtomicInteger count = new AtomicInteger();
        private volatile long resetAt = System.currentTimeMillis() + 60_000;

        int increment() {
            long now = System.currentTimeMillis();
            if (now > resetAt) {
                count.set(0);
                resetAt = now + 60_000;
            }
            return count.incrementAndGet();
        }
    }
}
