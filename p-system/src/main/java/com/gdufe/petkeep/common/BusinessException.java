package com.gdufe.petkeep.common;

import lombok.Getter;

/**
 * 业务异常，统一由 GlobalExceptionHandler 捕获处理
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
