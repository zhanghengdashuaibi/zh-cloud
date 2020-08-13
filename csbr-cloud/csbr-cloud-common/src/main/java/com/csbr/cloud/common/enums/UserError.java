package com.csbr.cloud.common.enums;

import lombok.Getter;

/**
 * @program: csbr-cloud
 * @description: 用户类错误
 * @author: Huanglh
 * @create: 2020-07-16 17:55
 **/
public enum UserError {
    // 用户错误标记
    ERROR_TAG("U"),
    // 用户错误一级宏观码
    ERROR("U0001"),

    // region ************ 数据 ************
    // 数据错误
    DATA_ERROR("U0010"),
    // 字段验证失败
    FIELD_VERIFY_FAIL("U0015"),
    // 数据对象为空
    OBJECT_IS_NULL("U0016"),
    // endregion 数据

    // region ************ 用户-注册登录 ************
    // 用户-注册登录类错误二级宏观码
    USER_LOGIN_ERROR("U0100"),
    // 账户输入错误
    ACCOUNT_INPUT_ERROR("U0101"),
    // 账户已存在
    ACCOUNT_EXIST("U0102"),
    // 密码错误
    PASSWORD_ERROR("U0103"),
    // 验证码错误
    VALIDATE_CODE_ERROR("U0104"),
    // 账户不存在
    ACCOUNT_NOT_EXIST("U0105"),
    // 本地 ThreadLocal 用户数据为空
    LOCAL_USERINFO_ISNULL("U0106"),
    // endregion 用户-注册登录
    ;

    @Getter
    private String code;

    UserError(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
