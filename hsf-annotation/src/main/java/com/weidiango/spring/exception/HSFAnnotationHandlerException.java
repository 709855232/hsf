package com.weidiango.spring.exception;

/**
 @author mht
 @date 2019/9/17 16:07 */
public class HSFAnnotationHandlerException extends RuntimeException{
    public HSFAnnotationHandlerException() {
        super();
    }

    public HSFAnnotationHandlerException(String message) {
        super(message);
    }

    public HSFAnnotationHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HSFAnnotationHandlerException(Throwable cause) {
        super(cause);
    }

    protected HSFAnnotationHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
