package com.csbr.cloud.es.domain.vo;

import com.csbr.cloud.es.entity.ElasticEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangheng
 * @date 2020/7/16 17:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticDataVo<T> {

    /**
     * 索引名
     */
    private String idxName;
    /**
     * 数据存储对象
     */
    private ElasticEntity elasticEntity;
}
