package com.csbr.cloud.mybatis.config;

import lombok.Getter;

/**
 * @author HelloWoodes
 */

@Getter
public enum DataSourceKey {
    /**
     * Order data source key.
     */
    MASTER,
    /**
     * Storage data source key.
     */
    SLAVE,

}