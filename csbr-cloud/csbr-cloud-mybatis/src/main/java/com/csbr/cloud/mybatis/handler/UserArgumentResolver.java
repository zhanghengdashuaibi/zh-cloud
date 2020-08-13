package com.csbr.cloud.mybatis.handler;

import com.csbr.cloud.mybatis.annotation.CurrentUser;
import com.csbr.cloud.mybatis.entity.UserInfo;
import com.csbr.cloud.mybatis.interceptor.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author zhangheng
 * @date 2020/7/9 16:33
 */

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final Logger logger = LoggerFactory.getLogger(UserArgumentResolver.class);

    /**
     * 过滤出符合条件的参数，这里指的是加了 CurrentUser 注解的参数
     *
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        AnnotatedElement annotatedElement = parameter.getAnnotatedElement();
        Annotation[] annotations = annotatedElement.getAnnotations();
        logger.info(annotations.toString());
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }


//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return parameter.getParameterType().isAssignableFrom(UserInfo.class)
//                && parameter.hasParameterAnnotation(CurrentUser.class);
//    }

    @Override
    public UserInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest
            webRequest, WebDataBinderFactory binderFactory) {
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        UserInfo userInfo = UserContextHolder.get();
        return userInfo;
    }


//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        UserInfo userInfo = (UserInfo) webRequest.getAttribute("currentUser", RequestAttributes.SCOPE_REQUEST);
//        if (userInfo != null) {
//            return userInfo;
//        }
//        throw new MissingServletRequestPartException("currentUser");
//    }

}
