package com.csbr.cloud.es.enums;

import lombok.Getter;

/**
 * @author zhangheng
 * @date 2020/7/20 15:17
 */
@Getter
public enum FieldType {

    /**
     * text(会被分词) keyword(字段不会被分词)字符串类型
     */
    TEXT("text"),

    KEYWORD("keyword"),

    /**
     * 数值类型 long integer short byte double float half_float scaled_float
     */
    LONG("long"),

    INTEGER("integer"),

    SHORT("short"),

    BYTE("byte"),

    DOUBLE("double"),

    FLOAT("float"),

    HALF_FLOAT("half_float"),

    SCALED_FLOAT("scaled_float"),

    /**
     * 日期型 date date_nanos
     */
    DATE("date"),

    DATE_NANOS("date_nanos"),

    /**
     * 布尔型 boolean
     */
    BOOLEAN("boolean"),

    /**
     * 二进制 binary
     */
    BINARY("binary"),

    /**
     * 单条数据
     */
    OBJECT("object"),

    /**
     * 嵌套数组
     */
    NESTED("nested"),


    ;


    FieldType(String type){
        this.type = type;
    }

    private String type;

}
