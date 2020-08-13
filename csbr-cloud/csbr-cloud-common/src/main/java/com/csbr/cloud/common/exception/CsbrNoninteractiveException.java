package com.csbr.cloud.common.exception;

import com.csbr.cloud.common.enums.NoninteractiveError;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: csbr-cloud
 * @description: 非与外部交互类接口异常
 * @author: Huanglh
 * @create: 2020-07-16 17:44
 **/
@Slf4j
@Data
public class CsbrNoninteractiveException extends CsbrBaseException {

    public CsbrNoninteractiveException(NoninteractiveError code, String message) {
        super(code.toString(), message);
    }

    public CsbrNoninteractiveException(NoninteractiveError code, String message, Throwable cause) {
        super(code.toString(), message, cause);
    }

    public CsbrNoninteractiveException(String message) {
        super(NoninteractiveError.ERROR.toString(), message);
    }

    public CsbrNoninteractiveException(String code, String message) {
        super(NoninteractiveError.ERROR_TAG.toString(), code, message);
    }
}
