package com.csbr.cloud.mybatis.entity;

import lombok.Data;

/**
 * @program: user-auth-service
 * @description: 分页dto基类
 * @author: Huanglh
 * @create: 2020-07-13 16:01
 **/
@Data
public class BasePageDTO {
    private Long pageIndex = 1L;
    private Long pageSize = 10L;
}
