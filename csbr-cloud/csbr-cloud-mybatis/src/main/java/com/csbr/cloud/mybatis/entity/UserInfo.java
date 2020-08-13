package com.csbr.cloud.mybatis.entity;

import lombok.*;

/**
 * @author zhangheng
 * @date 2020/7/9 16:19
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String userId;

    private String userName;

    /** 平台GUID */
    private String platformGuid;
}
