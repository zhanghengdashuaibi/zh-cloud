package com.csbr.cloud.common.exception;

import com.csbr.cloud.common.enums.SystemError;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: csbr-cloud
 * @description: 系统类异常
 * @author: Huanglh
 * @create: 2020-07-16 17:44
 **/
@Slf4j
@Data
public class CsbrSystemException extends CsbrBaseException {

    public CsbrSystemException(SystemError code, String message) {
        super(code.toString(), message);
    }

    public CsbrSystemException(SystemError code, String message, Throwable cause) {
        super(code.toString(), message, cause);
    }

    public CsbrSystemException(String message) {
        super(SystemError.ERROR.toString(), message);
    }

    public CsbrSystemException(String code, String message) {
        super(SystemError.ERROR_TAG.toString(), code, message);
    }
}
