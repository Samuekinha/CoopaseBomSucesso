package com.example.moinho.Exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final String redirectPath;

    public BusinessException(String message, String redirectPath) {
        super(message);
        this.redirectPath = redirectPath;
    }

    public BusinessException(String message, String redirectPath, Throwable cause) {
        super(message, cause);
        this.redirectPath = redirectPath;
    }

}

