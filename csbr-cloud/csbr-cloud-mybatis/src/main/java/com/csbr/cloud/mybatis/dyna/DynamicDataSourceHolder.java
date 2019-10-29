package com.csbr.cloud.mybatis.dyna;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangheng
 * @date 2019/8/26 15:14
 */
public class DynamicDataSourceHolder {

    private final static Logger logger= LoggerFactory.getLogger(DynamicDataSourceHolder.class);

    //本地线程
    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();
    public static final String DB_MASTER = "master";
    public static final String DB_SLAVE = "slave";

    public static String getDbType() {
        String db = contextHolder.get();
        if (db == null) {
            db = DB_MASTER;
        }
        return db;
    }

    /**
     * 设置数据源
     * @param str
     */
    public static void setDBType(String str) {
        logger.info("数据源为:" + str);
        contextHolder.set(str);
    }

    /**
     * 清楚数据源
     */
    public static void clearDbType() {
        contextHolder.remove();
    }
}
