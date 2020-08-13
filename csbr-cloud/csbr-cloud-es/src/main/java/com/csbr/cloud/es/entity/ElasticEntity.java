package com.csbr.cloud.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author zhangheng
 * @date 2020/7/16 17:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticEntity<T> {

    /**
     * 主键标识，用户ES持久化
     */
    private String id;

    /**
     * JSON对象，实际存储数据
     */
    private Map data;
}
