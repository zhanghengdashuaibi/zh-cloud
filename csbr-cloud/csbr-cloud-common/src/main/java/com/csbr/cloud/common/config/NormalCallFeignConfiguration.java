package com.csbr.cloud.common.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 普通调用Feign配置
 * @author arthas on 2019/10/15
 */
@Configuration
public class NormalCallFeignConfiguration {
    /**
     * 普通调用配置 连接超时为5s，读取超时为30s，适用于接口响应速度普通的FeignClient
     * @return
     */
    @Bean
    public Request.Options normalCallOptions(){
        return new Request.Options(5000,30000);
    }
}
