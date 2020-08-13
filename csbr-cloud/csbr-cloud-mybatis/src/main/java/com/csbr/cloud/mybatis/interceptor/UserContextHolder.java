package com.csbr.cloud.mybatis.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangheng
 * @date 2020/7/13 15:51
 * 使用共享线程保存用户信息 使之在微服务之间传递
 */
public class UserContextHolder {

    //日志
    private final static Logger logger = LoggerFactory.getLogger(UserContextHolder.class);
    private static final TransmittableThreadLocal context = new TransmittableThreadLocal();
//    static ExecutorService pool =  TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(2));
//    static ThreadLocal context = new InheritableThreadLocal<>();
    /**
     * 设置用户信息
     * @param t  -- 用户信息
     */
    public static <T> void set(T t) {
        context.set(t);//set方法已经调用remove()
        logger.info("ThreadID:{}, threadLocal {}", Thread.currentThread().getId(), JSON.toJSONString(t));
    }

    /**
     * 获取用户信息
     * @return -- 用户信息
     */
    public static <T> T get() {
        T t = (T) context.get();
        logger.info("ThreadID:{}, threadLocal set {}", Thread.currentThread().getId(), JSON.toJSONString(context.get()));
        return t;
    }

    /**
     * 移除
     */
    public static void remove(){
        context.remove();
        logger.info("共享线程已清除!!!");
    }
}
