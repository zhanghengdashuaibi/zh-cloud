package com.csbr.cloud.canal.annotation.table;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.csbr.cloud.canal.annotation.ListenPoint;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 重命名表
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ListenPoint(eventType = CanalEntry.EventType.RENAME)
public @interface RenameTableListenPoint {
	
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
