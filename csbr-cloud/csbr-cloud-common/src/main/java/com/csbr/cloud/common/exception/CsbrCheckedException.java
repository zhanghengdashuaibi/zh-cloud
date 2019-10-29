package com.csbr.cloud.common.exception;

/**
 * @author zhangheng
 * @date 2019/10/24 17:18
 */
public class CsbrCheckedException extends RuntimeException{

    public CsbrCheckedException(String message) {
        super(message);
    }

    public CsbrCheckedException(Throwable cause) {
        super(cause);
    }

    public CsbrCheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsbrCheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
