package com.csbr.cloud.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * @program: user-auth-service
 * @description: tools
 * @author: Huanglh
 * @create: 2020-07-03 21:26
 **/
@Component
@Configuration
public class CsbrBeanUtil {

    /**
     * 类型-属性字典
     */
    private static final Map<Class<?>, Set<String>> TYPE_PROPERTIES;

    static {
        TYPE_PROPERTIES = new HashMap<>();
    }

    /**
     * 将对象转换为指定类型的对象
     *
     * @param clazz        目标类型对象
     * @param <S>          源数据对象类型
     * @param <T>          目标对象类型
     * @param openPlusCopy 是否开启属性拷贝plus
     * @return 结果
     */
    public <S, T> T convert(S source, Class<T> clazz, Boolean openPlusCopy) {
        if (source == null) {
            return null;
        }

        Set<String> sourceProps = getProperties(source);

        if (sourceProps == null || sourceProps.isEmpty()) {
            return null;
        }

        try {
            T target = clazz.newInstance();
            BeanUtils.copyProperties(source, target);
            if (openPlusCopy) {
                copyPropertiesPlus(source, target);
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public <S, T> T convert(S source, Class<T> clazz) {
        return convert(source, clazz, false);
    }

    /**
     * 将指定类型的对象集合转换为目标类型对象集合
     *
     * @param sources      元数据对象集合
     * @param clazz        目标类型对象
     * @param <S>          源数据对象类型
     * @param <T>          目标对象类型
     * @param openPlusCopy 是否开启属性拷贝plus
     * @return 结果
     */
    public <S, T> List<T> convert(Collection<S> sources, Class<T> clazz, Boolean openPlusCopy) {
        if (sources == null || sources.isEmpty()) {
            return null;
        }

        List<T> targets = new LinkedList<>();

        for (S item : sources) {
            targets.add(convert(item, clazz, openPlusCopy));
        }

        return targets;
    }

    public <S, T> List<T> convert(Collection<S> sources, Class<T> clazz) {
        return convert(sources, clazz, false);
    }

    /**
     * 获取对象的属性列表.
     *
     * @param object 对象
     * @param <T>    对象类型
     * @return 属性列表
     */
    public <T> Set<String> getProperties(T object) {
        Class<?> clazz = object.getClass();

        return getProperties(clazz);
    }

    /**
     * 获取对象的属性列表
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Set<String> getProperties(Class<T> clazz) {
        if (!TYPE_PROPERTIES.containsKey(clazz)) {
            try {
                PropertyDescriptor[] pDs = BeanUtils.getPropertyDescriptors(clazz);
                Set setProps = new HashSet<>();
                for (PropertyDescriptor pD : pDs) {
                    setProps.add(pD.getName());
                }

                TYPE_PROPERTIES.put(clazz, setProps);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return TYPE_PROPERTIES.get(clazz);
    }

    /**
     * 属性拷贝plus
     * 支持将String类型属性拷贝到对应名称的 Map类型、Enum类型 的属性
     *
     * @param source
     * @param target
     */
    private void copyPropertiesPlus(Object source, Object target) {
        // 获取数据源模型对应名称属性的值，并做转换
        Class<?> sourceClazz = source.getClass();
        PropertyDescriptor[] sourcePDs = BeanUtils.getPropertyDescriptors(sourceClazz);
        Map<String, PropertyDescriptor> sourceParams = new HashMap<>();
        for (PropertyDescriptor sourcePD : sourcePDs) {
            sourceParams.put(sourcePD.getName(), sourcePD);
        }

        // 获取目标模型中是否存在 MAP 或者 ENUM 的属性
        Class<?> targetClazz = target.getClass();
        PropertyDescriptor[] targetPDs = BeanUtils.getPropertyDescriptors(targetClazz);
        for (PropertyDescriptor targetPD : targetPDs) {
            try {
                // 检查属性类型是否是 MAP ,是则记录
                if (targetPD.getPropertyType().isAssignableFrom(Map.class)) {
                    String sourceVal = getSourceValue(sourceParams, targetPD, source);
                    if (sourceVal != null && !sourceVal.isEmpty()) {
                        Method writeMethod = targetPD.getWriteMethod();
                        writeMethod.invoke(target, JSON.parseObject(sourceVal, Map.class));
                    }
                }
                // 检查属性类型是否是 ENUM ,是则记录
                if (targetPD.getPropertyType().isEnum()) {
                    String sourceVal = getSourceValue(sourceParams, targetPD, source);
                    if (sourceVal != null) {
                        Object[] items = targetPD.getPropertyType().getEnumConstants();
                        for (Object item : items) {
                            if (item.toString().equals(sourceVal)) {
                                Method writeMethod = targetPD.getWriteMethod();
                                writeMethod.invoke(target, item);
                                break;
                            }
                        }
                    }
                }
                // 检查属性类型是否是 Timestamp ,是则记录
                if (targetPD.getPropertyType().isAssignableFrom(Timestamp.class)) {
                    String sourceVal = getSourceValue(sourceParams, targetPD, source);
                    if (StringUtils.isNotEmpty(sourceVal)) {
                        Method writeMethod = targetPD.getWriteMethod();
                        writeMethod.invoke(target, Timestamp.valueOf(sourceVal));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取数据源属性的值
     *
     * @param sourceParams
     * @param targetPD
     * @param source
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private String getSourceValue(Map<String, PropertyDescriptor> sourceParams,
                                  PropertyDescriptor targetPD,
                                  Object source) throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor sourcePD = sourceParams.get(targetPD.getName());
        if (sourcePD != null) {
            Method readMethod = sourcePD.getReadMethod();
            // 调用属性的get方法获取属性值
            Object sourceVal = readMethod.invoke(source);
            // 判断属性值为String，才能继续做反序列化或反射操作
            if (sourceVal instanceof String) {
                return (String) sourceVal;
            }
        }
        return null;
    }

    /**
     * 获取对象属性名称和属性值
     *
     * @param obj
     * @return
     */
    public Map<String, Object> getPdDictionary(Object obj) {
        Map<String, Object> res = new HashMap<>();
        Class<?> clazz = obj.getClass();

        PropertyDescriptor[] pDs = BeanUtils.getPropertyDescriptors(clazz);

        for (PropertyDescriptor pD : pDs) {
            if (pD != null && !pD.getPropertyType().equals(Class.class)) {
                Method getMethod = pD.getReadMethod();

                try {
                    res.put(pD.getName(), getMethod.invoke(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return res;
    }

}
