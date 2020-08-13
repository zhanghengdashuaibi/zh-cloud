package com.csbr.cloud.mybatis.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csbr.cloud.mybatis.entity.UserInfo;
import com.csbr.cloud.mybatis.interceptor.UserContextHolder;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import io.jsonwebtoken.Claims;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;

/**
 * @author zhangheng
 * @date 2020/7/14 10:49
 * 用户信息aop
 */
@Aspect
@Component
@Configuration
@Slf4j
public class UserInfoAspect {


    @Pointcut("execution(* com.csbr.cloud.*.*.controller.*.*(..))")
    private void cloudPoint() {
    }

    @Pointcut("execution(* com.csbr.qingcloud.*..*.controller.*.*(..))")
    private void qingCloudPoint() {
    }

    @Pointcut("cloudPoint() || qingCloudPoint()")
    public void beforeUserInfo() {
    }//签名，可以理解成这个切入点的一个名称

    //前置
    @Before("beforeUserInfo()")
    public void before(JoinPoint joinPoint) throws Throwable {
        log.info("进入切面.......");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String authHeader = request.getHeader("X-USERINFO");//token信息
//        JSONObject attribute = (JSONObject) request.getAttribute("X-USERINFO");
        if (StringUtils.isNotBlank(authHeader)) {
            try {
                authHeader = URLDecoder.decode(authHeader, "UTF-8");
                log.info("aop:" + authHeader);
                UserInfo userInfo = JSON.parseObject(authHeader, UserInfo.class);
                //将用户信息放入上下文中
                UserContextHolder.set(userInfo);
            } catch (Exception e) {
                log.error("init userInfo error", e);
            }
        }
//        else{
//            if(!org.springframework.util.StringUtils.isEmpty(authHeader)){
//                String token = authHeader.replace("Bearer ", "");
//                Claims claims = JwtUtil.getClaims(token);
//                if(claims!=null){
//                    try {
//                        String userId = claims.get("userId") + "";
//                        String userName = claims.get("userName") + "";
//                        UserInfo userInfo = new UserInfo();
//                        userInfo.setUserId(userId);
//                        userInfo.setUserName(userName);
//                        UserContextHolder.set(userInfo);
//                    }catch (ExpiredJwtException expiredJwtEx){
//                        logger.error("token : {} 过期", token);
//
//                    }
//                }else{
//                    logger.error("token : {} 无效", token);
//                }
//            }
//        }
    }

    /**
     * 方法之后调用
     */
    @AfterReturning(pointcut = "beforeUserInfo()")
    public void doAfterReturning() {

    }
}
