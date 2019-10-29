package com.csbr.cloud.mybatis.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.csbr.cloud.mybatis.dyna.DynamicDataSourceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.SystemException;

/**
 * @author zhangheng
 * @date 2019/8/26 14:09
 * mybatis-plus配置类
 */
@Configuration
@EnableConfigurationProperties
//@MapperScan(basePackages = {"com.csbr.qingcloud.*.*.mapper"})
public class MybatisPlusConfig {

    /**
     * 分页插件配置
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor page = new PaginationInterceptor();
        //设置方言类型
        page.setDialectType("mysql");
        return page;
    }

    /***
     * plus 的性能优化
     * @return
     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//        /*<!-- SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长 -->*/
//        performanceInterceptor.setMaxTime(1000);
//        /*<!--SQL是否格式化 默认false-->*/
//        performanceInterceptor.setFormat(true);
//        return performanceInterceptor;
//    }


    /**
     * 配置数据源
     * 主库
     */
//    @Bean(name = "master")
//    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.master")
//    public DataSource master() {
//        return DataSourceBuilder.create().build();
//    }

    /**
     * 从库
     */
//    @Bean(name = "slave")
//    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.slave")
//    public DataSource slave() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Primary
//    @Bean(name = "dynamicDataSource")
//    public DynamicDataSource dataSource(@Qualifier("master") DataSource master,
//                                        @Qualifier("slave") DataSource slave) {
//        Map<Object, Object> targetDataSource = new HashMap<>();
//        targetDataSource.put(DynamicDataSourceHolder.DB_MASTER, master);
//        targetDataSource.put(DynamicDataSourceHolder.DB_SLAVE, slave);
//        DynamicDataSource dataSource = new DynamicDataSource();
//        dataSource.setTargetDataSources(targetDataSource);
//        return dataSource;
//    }


    @Bean
    public DynamicDataSourceInterceptor dynamicDataSourceInterceptor(){
        return new DynamicDataSourceInterceptor();
    }
    /**
     * 根据数据源创建SqlSessionFactory
     */
//    @Bean(name = "SqlSessionFactory")
//    public SqlSessionFactory test1SqlSessionFactory()
//            throws Exception {
//        //配置mybatis,对应mybatis-config.xml
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        //懒加载
//        LazyConnectionDataSourceProxy p=new LazyConnectionDataSourceProxy();
//        p.setTargetDataSource(dataSource(master(),slave()));
//        sqlSessionFactory.setDataSource(p);
//        //需要mapper文件时加入扫描，sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/hr.mapper/*/*Mapper.xml"));
//        MybatisConfiguration configuration = new MybatisConfiguration();
//        configuration.setJdbcTypeForNull(JdbcType.NULL);
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.setUseGeneratedKeys(true);
//        configuration.setCacheEnabled(false);
//        sqlSessionFactory.setConfiguration(configuration);
//        //加入上面的两个拦截器
//        Interceptor interceptor[]={paginationInterceptor(),dynamicDataSourceInterceptor()};
//        sqlSessionFactory.setPlugins(interceptor);
//        return sqlSessionFactory.getObject();
//    }

    /**
     * 配置事务管理器
     */
//    @Bean
//    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
//        return new DataSourceTransactionManager(dataSource);
//    }

    /**
     * 分布式事务使用JTA管理，只需配置一个JtaTransactionManager
     * @return
     */
    /*atomikos事务管理器*/
    public UserTransactionManager userTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    public UserTransactionImp userTransactionImp() throws SystemException {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(5000);
        return userTransactionImp;
    }

    /**
     * jta分布式事务管理
     * @return
     * @throws SystemException
     */
    @Bean
    public JtaTransactionManager jtaTransactionManager() throws SystemException {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        jtaTransactionManager.setUserTransaction(userTransactionImp());
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }
}
