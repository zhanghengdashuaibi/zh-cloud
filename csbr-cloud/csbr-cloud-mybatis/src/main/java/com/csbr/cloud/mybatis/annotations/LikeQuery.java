package com.csbr.cloud.mybatis.annotations;


import com.csbr.cloud.mybatis.enums.LikeQueryEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模糊查询标识注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LikeQuery {
    // 指定%添加的位置
    LikeQueryEnum type() default LikeQueryEnum.RIGHT;
}
