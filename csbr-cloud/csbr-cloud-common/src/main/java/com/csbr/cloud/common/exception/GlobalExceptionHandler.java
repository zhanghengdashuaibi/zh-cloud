/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.csbr.cloud.common.exception;

import com.csbr.cloud.common.enums.UserError;
import com.csbr.cloud.common.response.CommonRes;
import com.csbr.cloud.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局的的异常处理器
 */
@Slf4j
@RestControllerAdvice
@Configuration
public class GlobalExceptionHandler {

    /**
     * 全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonRes exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);

        return new CommonRes(ResponseCode.Fail.FAIL_CODE, e);
    }

    /**
     * 参数异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonRes bodyValidExceptionHandler(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.warn(fieldErrors.get(0).getDefaultMessage());


        return new CommonRes(UserError.FIELD_VERIFY_FAIL.getCode(), fieldErrors.get(0).getDefaultMessage());
    }


    /**
     * 自定义异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({CsbrCheckedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonRes cehckExceptionHandler(CsbrCheckedException exception) {
        exception.printStackTrace();
        return new CommonRes(ResponseCode.Cehck.ERROR_CODE, exception.getMessage());
    }

    // region 自有异常
    @ExceptionHandler({CsbrBaseException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonRes CsbrExceptionHandler(CsbrBaseException ex) {
        return CsbrExceptionHandlerBody(ex);
    }

    private CommonRes CsbrExceptionHandlerBody(CsbrBaseException exception) {
        String message = "Others type error: ";
        if (exception instanceof CsbrUserException) {
            message = "User type error: ";
        }
        if (exception instanceof CsbrSystemException) {
            message = "System type error: ";
        }
        if (exception instanceof CsbrNoninteractiveException) {
            message = "Noninteractive interface type error: ";
        }

        log.error(message + "{}", exception.getMessage(), exception);

        // 如果有异常来源，则将异常来源信息填入到返回jason的data属性中
        String causeMsg = "";
        if (exception.getCause() != null) {
            causeMsg = exception.getCause().getMessage();
        }

        return CommonRes.build(exception.getErrorCode(), exception.getMessage(), causeMsg);
    }
    // endregion
}
