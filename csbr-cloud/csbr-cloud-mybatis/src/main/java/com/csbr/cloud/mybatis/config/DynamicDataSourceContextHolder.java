package com.csbr.cloud.mybatis.config;

import com.csbr.cloud.mybatis.enums.DataSourceType;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author HelloWoodes
 */
@Slf4j
public class DynamicDataSourceContextHolder {

//    private static final ThreadLocal<String> CONTEXT_HOLDER = ThreadLocal.withInitial(DataSourceKey.MASTER::name);
//
//    private static List<Object> dataSourceKeys = new ArrayList<>();
//
//    public static void setDataSourceKey(DataSourceKey key) {
//        CONTEXT_HOLDER.set(key.name());
//    }
//
//    public static String getDataSourceKey() {
//        return CONTEXT_HOLDER.get();
//    }
//
//    public static void clearDataSourceKey() {
//        CONTEXT_HOLDER.remove();
//    }
//
//    public static List<Object> getDataSourceKeys() {
//        return dataSourceKeys;
//    }

    /**
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     *  所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源的变量
     */
    public static void setDataSourceType(String dsType)
    {
        log.info("切换到{}数据源", dsType);
        CONTEXT_HOLDER.set(dsType);
    }

    /**
     * 获得数据源的变量
     */
    public static String getDataSourceType()
    {
        String target = CONTEXT_HOLDER.get();
        if(Objects.isNull(target)){
            return DataSourceType.MASTER.name();
        }
        return target;
    }

    /**
     * 清空数据源变量
     */
    public static void clearDataSourceType()
    {
        CONTEXT_HOLDER.remove();
    }
}