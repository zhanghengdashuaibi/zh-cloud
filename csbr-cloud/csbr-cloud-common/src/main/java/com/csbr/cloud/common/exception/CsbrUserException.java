package com.csbr.cloud.common.exception;

import com.csbr.cloud.common.enums.UserError;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: csbr-cloud
 * @description: 用户类异常
 * @author: Huanglh
 * @create: 2020-07-16 17:44
 **/
@Slf4j
@Data
public class CsbrUserException extends CsbrBaseException {

    public CsbrUserException(UserError code, String message) {
        super(code.toString(), message);
    }

    public CsbrUserException(UserError code, String message, Throwable cause) {
        super(code.toString(), message, cause);
    }

    public CsbrUserException(String message) {
        super(UserError.ERROR.toString(), message);
    }

    public CsbrUserException(String code, String message) {
        super(UserError.ERROR_TAG.toString(), code, message);
    }
}
