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

import com.csbr.cloud.common.response.CommonRes;
import com.csbr.cloud.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
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
public class GlobalExceptionHandler {

    /**
     * 全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static CommonRes exception(Exception e) {
        e.printStackTrace();
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
    public static CommonRes bodyValidExceptionHandler(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.warn(fieldErrors.get(0).getDefaultMessage());
        return new CommonRes(ResponseCode.Fail.FAIL_CODE, fieldErrors.get(0).getDefaultMessage());
    }


    /**
     * 自定义异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({CsbrCheckedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static CommonRes cehckExceptionHandler(CsbrCheckedException exception) {
        exception.printStackTrace();
        return new CommonRes(ResponseCode.Cehck.ERROR_CODE, exception.getMessage());
    }

}
