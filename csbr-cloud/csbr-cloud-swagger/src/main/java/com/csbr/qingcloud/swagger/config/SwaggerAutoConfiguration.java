package com.csbr.qingcloud.swagger.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiOperation;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spi.service.contexts.SecurityContextBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * swagger自动配置类
 *
 * @author arthas on 2019/10/17
 */
@Configuration
@EnableConfigurationProperties
public class SwaggerAutoConfiguration {

//    @Autowired
//    private TypeResolver typeResolver;

    /**
     * 读取配置文件中的swagger配置
     *
     * @return SwaggerProperties
     */
    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    /**
     * 设置Token(如果needAuth为false则返回空)
     *
     * @return ApiKey
     */
    private ApiKey apiKey(SwaggerProperties swaggerProperties) {
        if (!swaggerProperties.getNeedAuth()) {
            return null;
        }
        return new ApiKey("Authorization", "Authorization", "header");
    }

    /**
     * 设置security上下文(指定鉴权相关字段和需要鉴权URL,如果needAuth为false则返回空)
     *
     * @return
     */
    private SecurityContext securityContext(SwaggerProperties swaggerProperties) {
        if (!swaggerProperties.getNeedAuth()) {
            return null;
        }
        SecurityContextBuilder builder = SecurityContext.builder();
        builder.securityReferences(defaultAuth());
        if (!StringUtils.isEmpty(swaggerProperties.getSecurityPathRegex())) {
            builder.forPaths(PathSelectors.regex(swaggerProperties.getSecurityPathRegex()));
        } else {
            builder.forPaths(PathSelectors.any());
        }
        return builder.build();
    }

    /**
     * 配置全局鉴权
     *
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference("Authorization", authorizationScopes));
    }

    /**
     * 定义ui相关配置
     *
     * @return
     */
    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null)
                .build();
    }

    /**
     * 生成Docket(默认拦截的是ApiOperation注解所注释的方法,后期可以改成可配置的)
     *
     * @param swaggerProperties
     * @return
     */
    @Bean
    public Docket docket(SwaggerProperties swaggerProperties) {
        TypeResolver typeResolver = new TypeResolver();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //.apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDate.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(
                        newRule(typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class)));
        if (swaggerProperties.getNeedAuth()) {
            docket.securitySchemes(newArrayList(apiKey(swaggerProperties)))
                    .securityContexts(newArrayList(securityContext(swaggerProperties)));
        }

        return docket;
    }

    /**
     * 生成Api基本信息
     *
     * @param swaggerProperties
     * @return
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .termsOfServiceUrl(swaggerProperties.getVersion())
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .build();
    }
}
