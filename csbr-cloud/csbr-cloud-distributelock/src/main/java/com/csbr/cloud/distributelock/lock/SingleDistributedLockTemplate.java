package com.csbr.cloud.distributelock.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangheng
 * @date 2019/11/6 9:23
 * 实现分布式redisson锁模板
 */
public class SingleDistributedLockTemplate implements DistributedLockTemplate {
    private RedissonClient redissonClient;

    public SingleDistributedLockTemplate() {
    }

    public SingleDistributedLockTemplate(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    @Override
    public <T> T lock(DistributedLockCallback<T> callback, boolean fairLock) {
        return lock(callback, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
    }

    @Override
    public <T> T lock(DistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = getLock(callback.getLockName(), fairLock);
        try {
            lock.lock(leaseTime, timeUnit);
            return callback.process();
        } finally {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback, boolean fairLock) {
        return tryLock(callback, DEFAULT_WAIT_TIME, DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, fairLock);
    }

    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = getLock(callback.getLockName(), fairLock);
        try {
            if (lock.tryLock(waitTime, leaseTime, timeUnit)) {
                return callback.process();
            }
        } catch (InterruptedException e) {

        } finally {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
        return null;
    }

    private RLock getLock(String lockName, boolean fairLock) {
        RLock lock;
        if (fairLock) {
            lock = redissonClient.getFairLock(lockName);
        } else {
            lock = redissonClient.getLock(lockName);
        }
        return lock;
    }

    @Override
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    @Override
    public RLock lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    @Override
    public RLock lock(String lockKey, TimeUnit unit ,int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
