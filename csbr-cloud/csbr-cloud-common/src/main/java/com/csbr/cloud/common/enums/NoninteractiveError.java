package com.csbr.cloud.common.enums;

import lombok.Getter;

/**
 * @program: csbr-cloud
 * @description: 非交互接口类错误
 * @author: Huanglh
 * @create: 2020-07-16 17:55
 **/
public enum NoninteractiveError {
    // 非交互接口错误标记
    ERROR_TAG("R"),
    // 非交互接口错误一级宏观码
    ERROR("R0001"),

    // region ************  ************
    // 非交互接口-注册登录类错误二级宏观码
    USER_LOGIN_ERROR("R0100"),
    // endregion
    ;

    @Getter
    private String code;

    NoninteractiveError(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
