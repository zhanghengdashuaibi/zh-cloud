package com.csbr.cloud.canal.annotation;

import com.csbr.cloud.canal.config.CanalClientConfiguration;
import com.csbr.cloud.canal.config.CanalConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *开启 Canal 客户端
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({CanalConfig.class, CanalClientConfiguration.class})
public @interface EnableCanalClient {
}
