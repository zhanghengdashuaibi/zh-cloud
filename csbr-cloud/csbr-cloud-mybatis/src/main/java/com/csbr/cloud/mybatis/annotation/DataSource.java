package com.csbr.cloud.mybatis.annotation;

import com.csbr.cloud.mybatis.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * @author zhangheng
 * @date 2020/8/11 10:25
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource
{
    /**
     * 切换数据源名称
     */
    public DataSourceType value() default DataSourceType.MASTER;
}
