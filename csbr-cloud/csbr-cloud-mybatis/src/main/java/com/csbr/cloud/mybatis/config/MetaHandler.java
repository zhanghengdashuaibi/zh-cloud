package com.csbr.cloud.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.csbr.cloud.mybatis.entity.UserInfo;
import com.csbr.cloud.mybatis.interceptor.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhangheng
 * @date 2020/7/9 12:06
 */
@Component
public class MetaHandler implements MetaObjectHandler {

    //日志
    private final static Logger logger = LoggerFactory.getLogger(MetaHandler.class);
    /**
     * 新增数据执行
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {

//        if(metaObject.hasSetter("guid")){
//            this.setFieldValByName("guid", "", metaObject);
//        }
        UserInfo userInfo = UserContextHolder.get();
        logger.info("createUserName+++++++++++++++++++++" + userInfo.getUserName());
        logger.info("本地线程的用户信息:"+ UserContextHolder.get());
        if(metaObject.hasSetter("createTime")){
            this.setFieldValByName("createTime", new Date(), metaObject);
        }
        if(metaObject.hasSetter("createUserName")){
            this.setFieldValByName("createUserName", userInfo.getUserName(), metaObject);
        }
        if(metaObject.hasSetter("updateUserName")){
            this.setFieldValByName("updateUserName",userInfo.getUserName(), metaObject);
        }
        if(metaObject.hasSetter("updateTime")){
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
        if(metaObject.hasSetter("createUserId")){
            this.setFieldValByName("createUserId", userInfo.getUserId(), metaObject);
        }
        if(metaObject.hasSetter("")){
            this.setFieldValByName("updateUserId", userInfo.getUserId(), metaObject);
        }
    }

    /**
     * 更新数据执行
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        UserInfo userInfo = UserContextHolder.get();
        if(metaObject.hasSetter("updateTime")){
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
        if(metaObject.hasSetter("updateUserName")){
            this.setFieldValByName("updateUserName", userInfo.getUserName(), metaObject);
        }
        if(metaObject.hasSetter("updateUserId")){
            this.setFieldValByName("updateUserId", userInfo.getUserId(), metaObject);
        }
    }


}
