package com.csbr.cloud.mybatis.annotations;


import com.csbr.cloud.mybatis.enums.CompareQueryEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 比较查询标识注解(> >= < <=)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompareQuery {
    CompareQueryEnum sign();

    String field() default "";
}
