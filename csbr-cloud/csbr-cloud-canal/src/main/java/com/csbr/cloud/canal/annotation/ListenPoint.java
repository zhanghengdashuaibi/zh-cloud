package com.csbr.cloud.canal.annotation;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.lang.annotation.*;

/**
 * 监听数据库的操作
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListenPoint {

    /**
     * canal 指令
     * default for all
     * @return canal destination
     */
    String destination() default "";

    /**
     * 数据库实例
     * default for all
     * @return canal destination
     */
    String[] schema() default {};

    /**
     * 监听的表
     * default for all
     * @return canal destination
     */
    String[] table() default {};

    /**
     * 监听操作的类型
     * default for all
     * @return canal event type
     */
    CanalEntry.EventType[] eventType() default {};

}
