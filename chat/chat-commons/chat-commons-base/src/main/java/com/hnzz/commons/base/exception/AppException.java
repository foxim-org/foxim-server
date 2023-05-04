package com.hnzz.commons.base.exception;

/**
 * @author HB
 * @Classname AppException
 * @Date 2023/1/3 15:46
 * @Description 最大异常类
 */
public class AppException extends RuntimeException{
    public AppException() {
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
