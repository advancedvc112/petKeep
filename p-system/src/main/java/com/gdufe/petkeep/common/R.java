package com.gdufe.petkeep.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应封装
 * <p>
 * 所有 Controller 返回此类型，前端根据 code 判断成功/失败
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    /** 状态码：200=成功 */
    private int code;

    /** 提示信息 */
    private String message;

    /** 响应数据 */
    private T data;

    // ── 快捷工厂方法 ──────────────────────────────────

    public static <T> R<T> ok(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> fail(String message) {
        return new R<>(500, message, null);
    }

    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null);
    }

    /** 未认证 */
    public static <T> R<T> unauthorized(String message) {
        return new R<>(401, message, null);
    }
}
