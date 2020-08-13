package com.csbr.cloud.es.annotation;

import com.csbr.cloud.es.enums.AnalyzerType;
import com.csbr.cloud.es.enums.FieldType;

import java.lang.annotation.*;

/**
 * @author zhangheng
 * @date 2020/7/20 15:13
 * ES字段注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface ESFiled {

    FieldType type() default FieldType.TEXT;

    /**
     * 指定分词器
     * @return
     */
    AnalyzerType analyzer() default AnalyzerType.STANDARD;

}
