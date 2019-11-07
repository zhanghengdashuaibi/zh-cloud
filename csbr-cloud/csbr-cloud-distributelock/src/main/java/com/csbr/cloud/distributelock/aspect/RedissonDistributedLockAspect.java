package com.csbr.cloud.distributelock.aspect;

import com.csbr.cloud.distributelock.annotation.DistributedLock;
import com.csbr.cloud.distributelock.lock.DistributedLockCallback;
import com.csbr.cloud.distributelock.lock.DistributedLockTemplate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangheng
 * @date 2019/11/6 9:28
 */
@Aspect
@Component
@Order(1)
public class RedissonDistributedLockAspect {

    @Autowired
    private DistributedLockTemplate lockTemplate;

    @Pointcut("@annotation(com.csbr.cloud.distributelock.annotation.DistributedLock)")
    public void DistributedLockAspect() {}

    @Around(value = "DistributedLockAspect()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        //切点所在的类
        Class targetClass = pjp.getTarget().getClass();
        //使用了注解的方法
        String methodName = pjp.getSignature().getName();

        Class[] parameterTypes = ((MethodSignature)pjp.getSignature()).getMethod().getParameterTypes();

        Method method = targetClass.getMethod(methodName, parameterTypes);

        Object[] arguments = pjp.getArgs();

        final String lockName = getLockName(method, arguments);

        return lock(pjp, method, lockName);
    }

    @AfterThrowing(value = "DistributedLockAspect()", throwing="ex")
    public void afterThrowing(Throwable ex) {
        throw new RuntimeException(ex);
    }

    public String getLockName(Method method, Object[] args) {
        Objects.requireNonNull(method);
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);

        String lockName = annotation.lockName(),
                param = annotation.param();

        if (isEmpty(lockName)) {
            if (args.length > 0) {
                if (isNotEmpty(param)) {
                    Object arg;
                    if (annotation.argNum() > 0) {
                        arg = args[annotation.argNum() - 1];
                    } else {
                        arg = args[0];
                    }
                    lockName = String.valueOf(getParam(arg, param));
                } else if (annotation.argNum() > 0) {
                    lockName = args[annotation.argNum() - 1].toString();
                }
            }
        }

        if (isNotEmpty(lockName)) {
            String preLockName = annotation.lockNamePre(),
                    postLockName = annotation.lockNamePost(),
                    separator = annotation.separator();

            StringBuilder lName = new StringBuilder();
            if (isNotEmpty(preLockName)) {
                lName.append(preLockName).append(separator);
            }
            lName.append(lockName);
            if (isNotEmpty(postLockName)) {
                lName.append(separator).append(postLockName);
            }

            lockName = lName.toString();

            return lockName;
        }

        throw new IllegalArgumentException("Can't get or generate lockName accurately!");
    }

    /**
     * 从方法参数获取数据
     *
     * @param param
     * @param arg 方法的参数数组
     * @return
     */
    public Object getParam(Object arg, String param) {
        if (isNotEmpty(param) && arg != null) {
            try {
                PropertyUtils propertyUtils=new PropertyUtils();
                Object result = propertyUtils.getProperty((Class<? extends Object>) arg, param);
                return result;
            } catch (Exception e) {
                throw new RuntimeException("", e);
            }
        }
        return null;
    }

    public Object lock(ProceedingJoinPoint pjp, Method method, final String lockName) {

        DistributedLock annotation = method.getAnnotation(DistributedLock.class);

        boolean fairLock = annotation.fairLock();

        boolean tryLock = annotation.tryLock();

        if (tryLock) {
            return tryLock(pjp, annotation, lockName, fairLock);
        } else {
            return lock(pjp,lockName, fairLock);
        }
    }

    public Object lock(ProceedingJoinPoint pjp, final String lockName, boolean fairLock) {
        return lockTemplate.lock(new DistributedLockCallback<Object>() {
            @Override
            public Object process() {
                return proceed(pjp);
            }

            @Override
            public String getLockName() {
                return lockName;
            }
        }, fairLock);
    }

    public Object tryLock(ProceedingJoinPoint pjp, DistributedLock annotation, final String lockName, boolean fairLock) {

        long waitTime = annotation.waitTime(),
                leaseTime = annotation.leaseTime();
        TimeUnit timeUnit = annotation.timeUnit();

        return lockTemplate.tryLock(new DistributedLockCallback<Object>() {
            @Override
            public Object process() {
                return proceed(pjp);
            }

            @Override
            public String getLockName() {
                return lockName;
            }
        }, waitTime, leaseTime, timeUnit, fairLock);
    }

    public Object proceed(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    private boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }
}
