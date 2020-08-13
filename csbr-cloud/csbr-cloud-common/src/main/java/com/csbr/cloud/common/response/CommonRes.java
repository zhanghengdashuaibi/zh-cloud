package com.csbr.cloud.common.response;

import com.csbr.cloud.common.constant.ResponseConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author arthas on 2019/10/15
 */
@Data
public class CommonRes<T> implements Serializable {

    private static final long serialVersionUID = 2543916584910512259L;

    private String code;

    private String msg;

    private T data;

    private String at;

    public CommonRes() {
    }

    public CommonRes(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public CommonRes(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CommonRes(String code, String msg, T data, String at) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.at = at;
    }

    public static <T> CommonRes<T> ok() {
        return new CommonRes<>(ResponseCode.Success.SUCCESS_CODE, ResponseCode.Success.SUCCESS_MSG, null);
    }

    public static <T> CommonRes<T> ok(T data) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return new CommonRes<>(ResponseCode.Success.SUCCESS_CODE, ResponseCode.Success.SUCCESS_MSG, data, sdf.format(date));
    }

    public static <T> CommonRes<T> ok(String code, String msg, T data) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return new CommonRes<>(code, msg, data, sdf.format(date));
    }

    public static <T> CommonRes<T> ok(String msg, T data) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return new CommonRes<>(ResponseCode.Success.SUCCESS_CODE, msg, data, sdf.format(date));
    }

    // region build
    public static <T> CommonRes<T> build(String code, String msg, T data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new CommonRes<>(code, msg, data, sdf.format(new Date()));
    }

    public static <T> CommonRes<T> success(String msg, T data) {
        return build(ResponseCode.Success.SUCCESS_CODE, msg, data);
    }

    public static <T> CommonRes<T> success(T data) {
        return success(ResponseCode.Success.SUCCESS_MSG, data);
    }

    public static <T> CommonRes<T> success() {
        return success(null);
    }
    // endregion

//    public static <T> CommonRes<T> ok(String msg, T data) {
//
//        return new CommonRes<>(ResponseCode.Success.SUCCESS_CODE, msg, data, sdf.format(date));
//    }

    public static void securityFail(HttpServletResponse response,
                                    String msg, HttpStatus httpcode) {
        try {
            response.setCharacterEncoding(ResponseConstant.UTF8);
            response.setContentType(ResponseConstant.CONTENT_TYPE);
            response.setStatus(httpcode.value());
            ObjectMapper obj = new ObjectMapper();
            response.getWriter().print(
                    obj.writeValueAsString(new CommonRes<>(ResponseCode.Fail.FAIL_CODE, msg, null)));
            response.getWriter().flush();
        } catch (Exception e) {

        }
    }
}
