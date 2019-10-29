package com.csbr.cloud.common.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign表单提交配置类，使用表单提交的方法需要添加此类配置
 * @author arthas on 2019/10/15
 */
@Configuration
public class FormEncoderFeignConfiguration {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * 注册一个form编码器，实现支持form表单提交
     * @return
     */
    @Bean
    public Encoder feignFormEncoder(){
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
}
