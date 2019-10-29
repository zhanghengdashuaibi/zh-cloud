package com.csbr.cloud.common.config;

//import com.bluestar.cloud.feign.interceptor.InnerHeaderInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign基础配置类
 *
 * @author arthas on 2019/10/18
 */
@Configuration
public class BaseFeignConfiguration {
    /**
     * 注册拦截器添加token和inner等参数
     * @return RequestInterceptor
     */
//    @Bean
//    public RequestInterceptor innerHeaderInterceptor() {
//        return new InnerHeaderInterceptor();
//    }
}
