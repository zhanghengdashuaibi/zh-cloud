package com.csbr.cloud.mybatis.entity;

import lombok.Data;

import java.util.List;

/**
 * @program: csbr-cloud
 * @description: 层级结构VO基本属性
 * @author: yio
 * @create: 2020-07-23 13:58
 **/
@Data
public class HierarchicalVO {



    /** 子节点集合 */
    private List<HierarchicalVO> children;
}
