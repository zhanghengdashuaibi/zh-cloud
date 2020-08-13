package com.csbr.cloud.mybatis.util;


import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Configuration
public class ValidatorUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory()
            .getValidator();

    public <T> Map<String, StringBuffer> validate(T obj) {
        Map<String, StringBuffer> errorMap = null;
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (set != null && set.size() > 0) {
            errorMap = new HashMap<String, StringBuffer>();
            String property = null;
            for (ConstraintViolation<T> cv : set) {
                //这里循环获取错误信息，可以自定义格式
                property = cv.getPropertyPath().toString();
                if (errorMap.get(property) != null) {
                    errorMap.get(property).append("," + cv.getMessage());
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append(cv.getMessage());
                    errorMap.put(property, sb);
                }
            }
        }
        return errorMap;
    }

    public <T> String validateRetrunOne(T obj) {
        Map<String, StringBuffer> map = validate(obj);
        if (map != null && !map.isEmpty()) {
            return map.values().toArray()[0].toString();
        }
        return null;

    }

    /**
     * 返回所有消息字符串
     *
     * @param obj
     * @param <T>
     * @return
     */
    public <T> String validateRetrunAll(T obj) {
        Map<String, StringBuffer> map = validate(obj);
        StringBuffer buf = new StringBuffer();
        if (map != null) {
            for (StringBuffer buffer : map.values()) {
                buf.append(buffer);
                buf.append("\r\n");
            }
        }
        return buf.toString();

    }
}
