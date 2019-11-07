package com.csbr.cloud.distributelock.lock;

/**
 * @author zhangheng
 * @date 2019/11/5 18:17
 * redission分布式锁 回调接口
 */
public interface DistributedLockCallback<T> {

    /**
     * 调用者必须在此方法中实现需要加分布式锁的业务逻辑
     *
     * @return
     */
    public T process();

    /**
     * 得到分布式锁名称
     *
     * @return
     */
    public String getLockName();
}
