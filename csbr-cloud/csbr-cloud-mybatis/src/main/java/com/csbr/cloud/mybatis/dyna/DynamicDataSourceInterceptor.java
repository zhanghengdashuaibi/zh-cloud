package com.csbr.cloud.mybatis.dyna;

import com.csbr.cloud.mybatis.config.ExecutorConfig;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;

/**
 * @author zhangheng
 * @date 2019/8/26 15:16
 *
 * 数据源拦截器(主库为读库,从库为写库)
 */
//指定拦截哪些方法,update包括增删改
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }) })
public class DynamicDataSourceInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    //正则匹配含有 insert delete update名称
    private static final String REGEX=".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    //初始拦截
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //开启事务
        boolean synchronizationActive= TransactionSynchronizationManager.isActualTransactionActive();
        //默认从库(写库)
        String lookupKey=DynamicDataSourceHolder.DB_SLAVE;
        if(!synchronizationActive){
            Object[] objects=invocation.getArgs();
            MappedStatement ms=(MappedStatement)objects[0];
            if(logger.isInfoEnabled()){
                logger.info("ms.getSqlCommandType():[{}] ", ms.getSqlCommandType());
            }
            //如果为查询则使用主库
            if(ms.getSqlCommandType().equals(SqlCommandType.SELECT)){
                //如果selectKey为自增id查询主键,使用从库(写库)
                if(ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)){
                    lookupKey=DynamicDataSourceHolder.DB_SLAVE;
                }else{
                    BoundSql boundSql=ms.getSqlSource().getBoundSql(objects[1]);
                    String sql=boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]"," ");
                    if(logger.isInfoEnabled()){
                        logger.info("输出的sql:[{}] ", sql);
                    }
                    //匹配带有insert delete update等名称 使用从库
                    if(sql.matches(REGEX)){
                        lookupKey=DynamicDataSourceHolder.DB_SLAVE;
                    }else{
                        //主库走读库
                        lookupKey=DynamicDataSourceHolder.DB_MASTER;
                    }
                }
            }
        }else{
            lookupKey=DynamicDataSourceHolder.DB_SLAVE;
        }
        DynamicDataSourceHolder.setDBType(lookupKey);
        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target) {
        //增删改查的拦截，然后交由intercept处理
        if(target instanceof Executor){
            return Plugin.wrap(target,this);
        }else{
            return target;
        }
    }


    @Override
    public void setProperties(Properties properties) {

    }
}
