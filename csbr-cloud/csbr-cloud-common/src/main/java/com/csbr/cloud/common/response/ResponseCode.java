package com.csbr.cloud.common.response;

/**
 * 定义基础错误码
 * B 开头表示未分类的基础错误 BASE
 */
public interface ResponseCode {

    interface Success {
        String SUCCESS_CODE = "00000";
        String SUCCESS_MSG = "success";
    }

    interface Fail {
        String FAIL_MSG = "fail";

        String FAIL_CODE = "B0400";
    }

    interface Cehck {
        String ERROR_CODE = "B0500";
    }

    interface Error {
        String ERROR_CODE = "B0001";

        String ERROR_MSG = "error";
    }

}
