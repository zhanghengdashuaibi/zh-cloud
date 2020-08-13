package com.csbr.cloud.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbr.cloud.common.enums.UserError;
import com.csbr.cloud.common.exception.CsbrUserException;
import com.csbr.cloud.common.util.CsbrBeanUtil;
import com.csbr.cloud.mybatis.annotations.CompareQuery;
import com.csbr.cloud.mybatis.annotations.LikeQuery;
import com.csbr.cloud.mybatis.entity.BasePageDTO;
import com.csbr.cloud.mybatis.entity.FieldInfos;
import com.csbr.cloud.mybatis.entity.PageListVO;
import com.csbr.cloud.mybatis.entity.UserInfo;
import com.csbr.cloud.mybatis.interceptor.UserContextHolder;
import com.csbr.cloud.mybatis.service.CsbrService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: user-auth-service
 * @description:
 * @author: Huanglh
 * @create: 2020-07-09 18:01
 **/
public class CsbrServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements CsbrService<T> {
    @Autowired
    protected CsbrBeanUtil csbrBeanUtil;

    /**
     * 自动生成新增人员id和姓名的新增条件
     *
     * @param entity
     * @return
     */
    @Override
    public void csbrAddEntity(T entity) {
        Class<?> clazz = entity.getClass();

        UserInfo userInfo = UserContextHolder.get();
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new CsbrUserException(UserError.LOCAL_USERINFO_ISNULL, "User info is null!");
        }

        // 获取属性
        PropertyDescriptor[] pDs = BeanUtils.getPropertyDescriptors(clazz);

        for (PropertyDescriptor pD : pDs) {
            try {
                if (pD.getName().equals("createUserId")) {
                    pD.getWriteMethod().invoke(entity, userInfo.getUserId());
                }
                if (pD.getName().equals("createUserName")) {
                    pD.getWriteMethod().invoke(entity, userInfo.getUserName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自动生成更新人员id和姓名的新增条件
     *
     * @param entity
     * @return
     */
    @Override
    public void csbrUpdateEntity(T entity) {
        Class<?> clazz = entity.getClass();

        UserInfo userInfo = UserContextHolder.get();
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new CsbrUserException(UserError.LOCAL_USERINFO_ISNULL, "User info is null!");
        }

        // 获取属性
        PropertyDescriptor[] pDs = BeanUtils.getPropertyDescriptors(clazz);

        for (PropertyDescriptor pD : pDs) {
            try {
                if (pD.getName().equals("updateUserId")) {
                    pD.getWriteMethod().invoke(entity, userInfo.getUserId());
                }
                if (pD.getName().equals("updateUserName")) {
                    pD.getWriteMethod().invoke(entity, userInfo.getUserName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public LambdaUpdateWrapper<T> csbrUpdateWrapper(Class<T> clazz) {
        UpdateWrapper query = Wrappers.<T>update();

        UserInfo userInfo = UserContextHolder.get();
        if (ObjectUtils.isEmpty(userInfo)) {
            throw new CsbrUserException(UserError.LOCAL_USERINFO_ISNULL, "User info is null!");
        }

        // 获取所有属性
        Set<String> props = csbrBeanUtil.getProperties(clazz);

        // 检查并更新修改人的id 和 名称
        for (String prop : props) {
            // 修改人id
            if (prop.equalsIgnoreCase("updateUserId")) {
                query.set("update_user_id", userInfo.getUserId());
            }

            // 修改人名称
            if (prop.equalsIgnoreCase("updateUserName")) {
                query.set("update_user_name", userInfo.getUserName());
            }
        }

        return query.lambda();
    }

    @Override
    public QueryWrapper<T> csbrBaseQueryWrapper(Object source, Class<T> entityClazz) {
        QueryWrapper<T> query = Wrappers.query();
        Class<?> sourceClazz = source.getClass();
        // 源数据属性注解信息
        Field[] sourceFields = sourceClazz.getDeclaredFields();
//        Map<String, Field> sourceFieldMap = new HashMap<>();
//        for (Field sourceField : sourceFields) {
//            sourceFieldMap.put(sourceField.getName(), sourceField);
//        }
        Map<String, List<FieldInfos>> sourceFieldMap = new HashMap<>();
        for (Field field : sourceFields) {
            // LikeQuery注解
            LikeQuery likeQuery = field.getAnnotation(LikeQuery.class);
            if (likeQuery != null) {
                annotationMapPut(sourceFieldMap, field.getName(), likeQuery, null);
            }
            // CompareQuery注解
            CompareQuery compareQuery = field.getAnnotation(CompareQuery.class);
            if (compareQuery != null) {
                String key = field.getName();
                if (!compareQuery.field().isEmpty()) {
                    key = StringUtils.isCamel(compareQuery.field()) ? compareQuery.field() : StringUtils.underlineToCamel(compareQuery.field());
                }
                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(sourceClazz, field.getName());
                try {
                    Object pdVal = pd.getReadMethod().invoke(source);
                    if (ObjectUtils.isNotEmpty(pdVal)) {
                        annotationMapPut(sourceFieldMap, key, compareQuery, pdVal);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        // entity属性信息（获取属性值）
        T target = csbrBeanUtil.convert(source, entityClazz, true);
        Map<String, Object> propertiesInfos = csbrBeanUtil.getPdDictionary(target);

        // 循环entity属性
        for (Map.Entry<String, Object> objectEntry : propertiesInfos.entrySet()) {
            Boolean doFlag = false;
            // 如果属性值不为空，则需要拼装sql
            if (ObjectUtils.isNotEmpty(objectEntry.getValue())) {
                doFlag = true;
            } else {
                // 如果有CompareQuery注解，且注解属性中带有值，也需要拼装
                if (sourceFieldMap.containsKey(objectEntry.getKey())) {
                    for (FieldInfos infos : sourceFieldMap.get(objectEntry.getKey())) {
                        if (ObjectUtils.isNotEmpty(infos.getValue())) {
                            doFlag = true;
                            break;
                        }
                    }
                }
            }

            if (doFlag) {
                // 驼峰转下划线
                String colnum = StringUtils.camelToUnderline(objectEntry.getKey());
                // 如果没有任何注解，则直接生成 = 查询
                if (!sourceFieldMap.containsKey(objectEntry.getKey())) {
                    query.eq(colnum, objectEntry.getValue());
                } else {
                    for (FieldInfos infos : sourceFieldMap.get(objectEntry.getKey())) {
                        // 检查注解
                        // like 模糊查询
                        if (infos.getAnnotation() instanceof LikeQuery) {
                            switch (((LikeQuery) infos.getAnnotation()).type()) {
                                case RIGHT:
                                    query.likeRight(colnum, objectEntry.getValue());
                                    break;
                                case LEFT:
                                    query.likeRight(colnum, objectEntry.getValue());
                                    break;
                                case ALL:
                                    query.like(colnum, objectEntry.getValue());
                                    break;
                                default:
                                    // 不做操作
                            }
                        }

                        // 比较查询 > >= < <=
                        // 比较查询的值，先从 source map 拿，没有再找 entity 拿
                        if ((ObjectUtils.isNotEmpty(objectEntry.getValue()) || ObjectUtils.isNotEmpty(infos.getValue()))
                                && infos.getAnnotation() instanceof CompareQuery) {
                            Object val = ObjectUtils.isNotEmpty(infos.getValue()) ? infos.getValue() : objectEntry.getValue();
                            // 如果值为 Date ,则转换成 Timestamp
                            if (val instanceof Date) {
                                val = new Timestamp(((Date) val).getTime());
                            }
                            switch (((CompareQuery) infos.getAnnotation()).sign()) {
                                case GT:
                                    query.gt(colnum, val);
                                    break;
                                case GE:
                                    query.ge(colnum, val);
                                    break;
                                case LT:
                                    query.lt(colnum, val);
                                    break;
                                case LE:
                                    query.le(colnum, val);
                                    break;
                                default:
                                    // 不做操作
                            }
                        }
                    }
                }
            }
        }

        return query;
    }

    @Override
    public LambdaQueryWrapper<T> csbrQueryWrapper(Object source, Class<T> entityClazz) {
       return csbrBaseQueryWrapper(source,entityClazz).lambda();
    }

    private void annotationMapPut(Map<String, List<FieldInfos>> map, String key, Annotation annotation, Object value) {
        List<FieldInfos> infosList = map.containsKey(key) ? map.get(key) : new ArrayList<>();
        FieldInfos infos = new FieldInfos();
        infos.setAnnotation(annotation);
        infos.setValue(value);
        infosList.add(infos);
        map.put(key, infosList);
    }

    /**
     * 分页查询
     *
     * @param dto
     * @param query
     * @return
     */
    @Override
    public <D extends BasePageDTO> PageListVO<T> csbrPageList(D dto, Wrapper<T> query) {
        Page<T> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
        IPage<T> iPage = baseMapper.selectPage(page, query);
        PageListVO<T> vo = new PageListVO().build(iPage);
        return vo;
    }

    /**
     * 根据guid查询数据是否存在
     *
     * @param guids
     * @param clazz
     * @return
     */
    @Override
    public boolean isExistsData(List<String> guids, Class<T> clazz) {
        QueryWrapper<T> query = Wrappers.query();
        if (!CollectionUtils.isEmpty(guids)) {
            if (guids.size() == 1) {
                query.eq("guid", guids.get(0));
            } else {
                query.in("guid", guids);
            }

            int cnt = baseMapper.selectCount(query);
            if (cnt > 0) {
                return true;
            }
        }


        return false;
    }

    /**
     * 查询结果按照指定的字段作为key生成map
     *
     * @param queryWrapper 查询条件
     * @param column       key字段
     * @return
     */
    @Override
    public Map<Object, T> csbrMap(Wrapper<T> queryWrapper, SFunction<T, ?> column) {
        Map<Object, T> res = new HashMap<>();
        List<T> entitys = baseMapper.selectList(queryWrapper);

        return entitys.stream().collect(Collectors.toMap(column, (e) -> e));

        // region 传统方法
//        String colName = this.getColName(column);
//        if (entitys.size() > 0) {
//            // 获取类型
//            T obj = entitys.get(0);
//            Class<?> clazz = obj.getClass();
//            for (T entity : entitys) {
//                // 获取指定字段的值
//                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, colName);
//                try {
//                    res.put(pd.getReadMethod().invoke(entity), entity);
//                } catch (Exception e) {
//                    throw new CsbrSystemException(SystemError.DATA_GET_ERROR, "Map 的 key 值获取失败：" + e.getMessage());
//                }
//            }
//        }
//        return res;
        // endregion 传统方法
    }

    // region 继承方法

    /**
     * 获取属性名称
     *
     * @param col
     * @param <T>
     * @return
     */
    protected <T> String getColName(SFunction<T, ?> col) {
        SerializedLambda lambda = LambdaUtils.resolve(col);
        return PropertyNamer.methodToProperty(lambda.getImplMethodName());
    }
    // endregion 继承方法
}
