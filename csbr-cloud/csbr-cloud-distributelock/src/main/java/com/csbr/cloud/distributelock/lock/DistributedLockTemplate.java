package com.csbr.cloud.distributelock.lock;

import jodd.introspector.Mapper;
import org.redisson.api.RLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangheng
 * @date 2019/11/5 18:18
 * redission 分布式锁的模板
 */
//@Component
//@Configuration
public interface DistributedLockTemplate {

    long DEFAULT_WAIT_TIME = 30;
    long DEFAULT_TIMEOUT   = 5;
    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 使用分布式锁，使用锁默认超时时间。
     * @param callback
     * @param fairLock 是否使用公平锁
     * @return
     */
    <T> T lock(DistributedLockCallback<T> callback, boolean fairLock);

    /**
     * 使用分布式锁。自定义锁的超时时间
     *
     * @param callback
     * @param leaseTime 锁超时时间。超时后自动释放锁。
     * @param timeUnit
     * @param fairLock 是否使用公平锁
     * @param <T>
     * @return
     */
    <T> T lock(DistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit, boolean fairLock);

    /**
     * 尝试分布式锁，使用锁默认等待时间、超时时间。
     * @param callback
     * @param fairLock 是否使用公平锁
     * @param <T>
     * @return
     */
    <T> T tryLock(DistributedLockCallback<T> callback, boolean fairLock);

    /**
     * 尝试分布式锁，自定义等待时间、超时时间。
     * @param callback
     * @param waitTime 获取锁最长等待时间
     * @param leaseTime 锁超时时间。超时后自动释放锁。
     * @param timeUnit
     * @param fairLock 是否使用公平锁
     * @param <T>
     * @return
     */
    <T> T tryLock(DistributedLockCallback<T> callback, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock);

    RLock lock(String lockKey);

    RLock lock(String lockKey, int timeout);

    RLock lock(String lockKey, TimeUnit unit, int timeout);

    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

}
