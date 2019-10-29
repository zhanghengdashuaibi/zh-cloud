package com.csbr.cloud.common.response;

public interface ResponseCode {

    interface Success {
        int SUCCESS_CODE = 1200;
        String SUCCESS_MSG = "sucess";
    }

    interface Fail {
        String FAIL_MSG = "fail";

        int FAIL_CODE = 2400;
    }

    interface Cehck {
        int ERROR_CODE = 3500;
    }

    interface Error {
        int ERROR_CODE = 4500;

        String ERROR_MSG = "error";
    }

}
