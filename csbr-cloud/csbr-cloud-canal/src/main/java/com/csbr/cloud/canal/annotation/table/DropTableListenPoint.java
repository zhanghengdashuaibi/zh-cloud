package com.csbr.cloud.canal.annotation.table;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.csbr.cloud.canal.annotation.ListenPoint;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 刪除表操作監聽器
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ListenPoint(eventType = CanalEntry.EventType.ERASE)
public @interface DropTableListenPoint {
	/**
	 * canal 指令
	 * default for all
	 */
	@AliasFor(annotation = ListenPoint.class)
	String destination() default "";
	
	/**
	 * 数据库实例
	 */
	@AliasFor(annotation = ListenPoint.class)
	String[] schema() default {};
}
