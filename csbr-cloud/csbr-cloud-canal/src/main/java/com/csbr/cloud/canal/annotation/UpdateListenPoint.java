package com.csbr.cloud.canal.annotation;

import com.alibaba.otter.canal.protocol.CanalEntry;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * 更新操作监听器
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ListenPoint(eventType = CanalEntry.EventType.UPDATE)
public @interface UpdateListenPoint {

    /**
     * canal 指令
     * default for all
     * @return canal destination
     */
    @AliasFor(annotation = ListenPoint.class)
    String destination() default "";

    /**
     * 数据库实例
     * default for all
     * @return canal destination
     */
    @AliasFor(annotation = ListenPoint.class)
    String[] schema() default {};

    /**
     * 监听的表
     * default for all
     * @return canal destination
     */
    @AliasFor(annotation = ListenPoint.class)
    String[] table() default {};

}
