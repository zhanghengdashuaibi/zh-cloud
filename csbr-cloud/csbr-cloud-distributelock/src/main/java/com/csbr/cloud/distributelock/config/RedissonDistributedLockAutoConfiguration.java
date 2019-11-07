package com.csbr.cloud.distributelock.config;

import com.csbr.cloud.distributelock.aspect.RedissonDistributedLockAspect;
import com.csbr.cloud.distributelock.lock.DistributedLockTemplate;
import com.csbr.cloud.distributelock.lock.SingleDistributedLockTemplate;
import com.csbr.cloud.distributelock.util.RedissLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author zhangheng
 * @date 2019/11/6 12:44
 */
@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonDistributedLockAutoConfiguration {

    @Autowired
    private RedissonProperties redssionProperties;

    @Bean
    public RedissonDistributedLockAspect redissonDistributedLockAspect(){
        return new RedissonDistributedLockAspect();
    }

    /**
     * 哨兵模式自动装配
     * @return
     */
//    @Bean
//    @ConditionalOnProperty(name="redisson.master-name")
//    RedissonClient redissonSentinel() {
//        Config config = new Config();
//        SentinelServersConfig serverConfig = config.useSentinelServers().addSentinelAddress(redssionProperties.getSentinelAddresses())
//                .setMasterName(redssionProperties.getMasterName())
//                .setTimeout(redssionProperties.getTimeout())
//                .setMasterConnectionPoolSize(redssionProperties.getMasterConnectionPoolSize())
//                .setSlaveConnectionPoolSize(redssionProperties.getSlaveConnectionPoolSize());
//
//        if(!StringUtils.isEmpty(redssionProperties.getPassword())) {
//            serverConfig.setPassword(redssionProperties.getPassword());
//        }
//        return Redisson.create(config);
//    }

//    @Bean(destroyMethod = "shutdown")
//    RedissonClient redisson()
//            throws IOException {
//        Config config = new Config();
////        Config config = Config.fromYAML(configFile.getInputStream());
//        return Redisson.create(config);
//    }

    /**
     * 单机模式
     * @param
     * @return
     */
    @Bean
    @ConditionalOnProperty(name="redisson.address")
    RedissonClient redissonSingle() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redssionProperties.getAddress())
                .setTimeout(redssionProperties.getTimeout())
                .setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());

        if(!StringUtils.isEmpty(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }

        return Redisson.create(config);
    }


    @Bean
    DistributedLockTemplate distributedLockTemplate(RedissonClient redissonClient) {
        SingleDistributedLockTemplate locker =new  SingleDistributedLockTemplate();
        locker.setRedissonClient(redissonClient);
        RedissLockUtil.setLocker(locker);
        return locker;
    }
}
