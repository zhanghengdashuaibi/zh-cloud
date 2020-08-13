package com.csbr.cloud.mybatis.entity;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * @program: common-admin-cloud-service
 * @description:
 * @author: Huanglh
 * @create: 2020-07-20 16:38
 **/
@Data
public class FieldInfos {
    private Annotation annotation;
    private Object value;
}
