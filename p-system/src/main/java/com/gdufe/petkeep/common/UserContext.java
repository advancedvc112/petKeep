package com.gdufe.petkeep.common;

/**
 * 用户上下文 — ThreadLocal 存储当前请求用户信息
 * <p>
 * 在 JwtInterceptor 中设值，请求结束后自动清理。
 * Controller / Service 通过 {@link #getUserId()} 获取当前用户 ID。
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<Integer> USER_ROLE = new ThreadLocal<>();

    public static void set(Long userId, Integer role) {
        USER_ID.set(userId);
        USER_ROLE.set(role);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static Integer getRole() {
        return USER_ROLE.get();
    }

    /** 是否是管理员 */
    public static boolean isAdmin() {
        Integer role = USER_ROLE.get();
        return role != null && role == 1;
    }

    /** 请求结束后必须调用，防止内存泄漏 */
    public static void clear() {
        USER_ID.remove();
        USER_ROLE.remove();
    }
}
