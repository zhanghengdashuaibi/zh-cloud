package com.csbr.cloud.mybatis.interceptor;

import com.alibaba.fastjson.JSON;
import com.csbr.cloud.mybatis.entity.UserInfo;
import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author zhangheng
 * @date 2020/7/13 11:01
 * seata 分布式事物 xid在feign中传递
 */
@Configuration
@ConditionalOnClass(Feign.class)
public class FeignInterceptor implements RequestInterceptor {

    //日志
    private final static Logger logger = LoggerFactory.getLogger(FeignInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {

        String xid = RootContext.getXID();
        if (StringUtils.isNotBlank(xid)) {
            logger.info("feign xid："+xid);
//            System.out.println("feign xid："+xid);
        }
        requestTemplate.header(RootContext.KEY_XID, xid);

        UserInfo user = UserContextHolder.get();
        logger.info("feign："+JSON.toJSONString(user));
        if (user != null) {
            try {
                String userJson = JSON.toJSONString(user);
                requestTemplate.header("X-USERINFO",new String[]{URLEncoder.encode(userJson,"UTF-8")});
//                requestTemplate.header("X-USERINFO",userJson);
            } catch (Exception e) {
                logger.error("用户信息设置错误",e);
            }
        }
    }
}
