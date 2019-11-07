package com.csbr.cloud.mybatis.dyna;

import com.csbr.cloud.mybatis.config.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * @author zhangheng
 * @date 2019/8/26 15:13
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("当前数据源 [{}]", DynamicDataSourceHolder.getDbType());
        return DynamicDataSourceHolder.getDbType();
    }
}
