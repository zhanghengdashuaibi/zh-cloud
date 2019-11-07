package com.csbr.cloud.distributelock.config;

import com.csbr.cloud.distributelock.lock.DistributedLock;
import com.csbr.cloud.distributelock.annotation.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
//import org.thymeleaf.annotation.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author zhangheng
 * @date 2019/7/17 11:05
 * 基于aop切面的redis的分布锁
 */
@Aspect
@Component
//@Configuration
@ConditionalOnClass(DistributedLock.class)//当存在DistributedLock类是才会DistributedLockAspectConfiguration类
//@AutoConfigureAfter(DistributedLockAutoConfiguration.class)//自动注入在DistributedLockAutoConfiguration加载前
public class RedisDistributedLockAspect {
    private final Logger logger = LoggerFactory.getLogger(RedisDistributedLockAspect.class);

    @Autowired
    private DistributedLock distributedLock;

    //自定义切点
    @Pointcut("@annotation(com.csbr.cloud.distributelock.annotation.RedisLock)")
    private void lockPoint(){

    }

    //环绕通知处理
    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        String key = redisLock.value();
        if(StringUtils.isEmpty(key)){
            Object[] args = pjp.getArgs();
            key = Arrays.toString(args);
        }
        int retryTimes = redisLock.action().equals(RedisLock.LockFailAction.CONTINUE) ? redisLock.retryTimes() : 0;
        boolean lock = distributedLock.lock(key, redisLock.keepMills(), retryTimes, redisLock.sleepMills());
        if(!lock) {
            logger.debug("get lock failed : " + key);
            return null;
        }

        //得到锁,执行方法，释放锁
        logger.debug("get lock success : " + key);
        try {
            return pjp.proceed();
        } catch (Exception e) {
            logger.error("execute locked method occured an exception", e);
        } finally {
            boolean releaseResult = distributedLock.releaseLock(key);
            logger.debug("release lock : " + key + (releaseResult ? " success" : " failed"));
        }
        return null;
    }
}
