package com.csbr.cloud.common.exception;

import lombok.Data;

/**
 * @program: csbr-cloud
 * @description:
 * @author: Huanglh
 * @create: 2020-07-16 17:11
 **/
@Data
public abstract class CsbrBaseException extends RuntimeException {
    /**
     * 错误码
     */
    protected String errorCode;

    public CsbrBaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CsbrBaseException(String Tag, String customCode, String message) {
        super(message);
        // 用户自定义错误码，只能定义后4位，第一位需具体异常指定
        String result = "";
        Integer strLength = 4;
        String fillStr = "0";
        for (int i = 0; i < strLength; i++) {
            result += fillStr;
        }
        result += customCode;
        this.errorCode = result.substring(result.length() - strLength);
    }

    public CsbrBaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CsbrBaseException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CsbrBaseException(String errorCode, String message, Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

}
