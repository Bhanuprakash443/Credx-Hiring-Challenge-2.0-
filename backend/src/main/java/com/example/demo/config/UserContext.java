package com.example.demo.config;

import com.example.demo.model.UserRole;

public class UserContext {
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<UserRole> userRole = new ThreadLocal<>();

    public static void set(Long id, UserRole role) {
        userId.set(id);
        userRole.set(role);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static UserRole getUserRole() {
        return userRole.get();
    }

    public static void clear() {
        userId.remove();
        userRole.remove();
    }
}
