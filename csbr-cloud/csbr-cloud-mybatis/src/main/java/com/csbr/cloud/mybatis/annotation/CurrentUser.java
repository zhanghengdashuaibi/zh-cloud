package com.csbr.cloud.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author zhangheng
 * @date 2020/7/9 16:18
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
