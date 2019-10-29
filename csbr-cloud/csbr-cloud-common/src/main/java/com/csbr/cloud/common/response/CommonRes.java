package com.csbr.cloud.common.response;

import com.csbr.cloud.common.constant.ResponseConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author arthas on 2019/10/15
 */
@Data
public class CommonRes<T> implements Serializable {

    private static final long serialVersionUID = 2543916584910512259L;

    private int code;

    private String msg;

    private T data;

    public CommonRes() {
    }

    public CommonRes(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public CommonRes(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> CommonRes<T> ok() {
        return new CommonRes<>(ResponseCode.Success.SUCCESS_CODE, ResponseCode.Success.SUCCESS_MSG, null);
    }

    public static <T> CommonRes<T> ok(T data) {
        return new CommonRes<>(ResponseCode.Success.SUCCESS_CODE, ResponseCode.Success.SUCCESS_MSG, data);
    }

    public static <T> CommonRes<T> ok(String msg, T data) {
        return new CommonRes<>(ResponseCode.Success.SUCCESS_CODE, msg, data);
    }

    public static void securityFail(HttpServletResponse response,
                                    String msg, HttpStatus httpcode) {
        try {
            response.setCharacterEncoding(ResponseConstant.UTF8);
            response.setContentType(ResponseConstant.CONTENT_TYPE);
            response.setStatus(httpcode.value());
            ObjectMapper obj = new ObjectMapper();
            response.getWriter().print(
                    obj.writeValueAsString(new CommonRes<>(ResponseCode.Fail.FAIL_CODE, msg,null)));
            response.getWriter().flush();
        } catch (Exception e) {

        }
    }
}
