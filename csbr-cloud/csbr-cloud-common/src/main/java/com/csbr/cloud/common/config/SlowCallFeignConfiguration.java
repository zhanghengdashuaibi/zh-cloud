package com.csbr.cloud.common.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 较慢调用Feign配置
 * @author arthas on 2019/10/15
 */
@Configuration
public class SlowCallFeignConfiguration {
    /**
     * 较慢调用配置 连接超时为5s，读取超时为50s，适用于接口响应速度较慢的FeignClient
     * @return
     */
    @Bean
    public Request.Options slowCallOptions(){
        return new Request.Options(5, TimeUnit.SECONDS,50,TimeUnit.SECONDS,true);
    }
}
