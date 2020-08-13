package com.csbr.cloud.common.enums;

import lombok.Getter;

/**
 * @program: csbr-cloud
 * @description: 系统类错误
 * @author: Huanglh
 * @create: 2020-07-16 17:55
 **/
public enum SystemError {
    // 系统错误标记
    ERROR_TAG("S"),
    // 系统错误一级宏观码
    ERROR("S0001"),

    // region ************ 数据 ************
    // 数据错误
    DATA_ERROR("S0010"),
    // 添加错误
    DATA_ADD_ERROR("S0011"),
    // 更新错误
    DATA_UPDATE_ERROR("S0012"),
    // 删除错误
    DATA_DEL_ERROR("S0013"),
    // 获取错误
    DATA_GET_ERROR("S0014"),
    // endregion 数据

    // region ************ 数据相关 ************
    // 系统-数据交换类错误二级宏观码
    DATA_RELATED_ERROR("S0100"),
    // 数据导入错误
    IMPORT_ERROR("S0101"),
    // 数据导出错误
    EXPORT_ERROR("S0102"),
    // endregion 数据导出导入

    // region ************ 文件相关(OBS等) ************
    // 系统-注册登录类错误二级宏观码
    FILE_RELATED_ERROR("S0200"),
    // 找不到路径
    CAN_NOT_FIND_PATH("S0201"),
    // endregion 系统-注册登录
    ;

    @Getter
    private String code;

    SystemError(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
