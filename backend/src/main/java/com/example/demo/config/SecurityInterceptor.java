package com.example.demo.config;

import com.example.demo.model.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String userIdStr = request.getHeader("X-User-Id");
        String userRoleStr = request.getHeader("X-User-Role");

        if (userIdStr != null && userRoleStr != null && !userIdStr.isEmpty() && !userRoleStr.isEmpty()) {
            try {
                Long userId = Long.parseLong(userIdStr);
                UserRole role = UserRole.valueOf(userRoleStr.toUpperCase());
                UserContext.set(userId, role);
            } catch (Exception e) {
                // Ignore parsing errors, keep context clean
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
