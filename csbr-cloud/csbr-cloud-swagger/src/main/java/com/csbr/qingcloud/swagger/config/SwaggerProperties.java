package com.csbr.qingcloud.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * swagger配置属性类
 *
 * @author arthas on 2019/10/17
 */
@Data
@Component
@ConfigurationProperties("csbr.swagger")
public class SwaggerProperties {
    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本
     */
    private String version = "1.0";

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系url地址
     */
    private String contactUrl;

    /**
     * 联系email
     */
    private String contactEmail;

    /**
     * 许可证
     */
    private String license;

    /**
     * 许可证url
     */
    private String licenseUrl;

    /**
     * 服务条款url
     */
    private String termsOfServiceUrl;

    /**
     * 是否需要鉴权
     */
    private Boolean needAuth = true;

    /**
     * 需要鉴权的路径正则
     */
    private String securityPathRegex;
}
