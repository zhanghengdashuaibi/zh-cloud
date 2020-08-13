package com.csbr.cloud.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbr.cloud.mybatis.entity.BasePageDTO;
import com.csbr.cloud.mybatis.entity.PageListVO;

import java.util.List;
import java.util.Map;

/**
 * @program: user-auth-service
 * @description:
 * @author: Huanglh
 * @create: 2020-07-09 17:57
 **/
public interface CsbrService<T> extends IService<T> {
    /**
     * 自动生成新增人员id和姓名的新增条件
     *
     * @param entity
     * @return
     */
    void csbrAddEntity(T entity);

    /**
     * 自动生成更新人员id和姓名的新增条件
     *
     * @param entity
     * @return
     */
    void csbrUpdateEntity(T entity);

    /**
     * 自动生成更新人员id和姓名的更新条件
     *
     * @param clazz
     * @return
     */
    LambdaUpdateWrapper<T> csbrUpdateWrapper(Class<T> clazz);


    /**
     * 有值的字段自动生成查询条件
     * 支持 like(LikeQuery)、比较(CompareQuery) 条件注解
     *
     * @param source 数据传递来源实例
     * @param clazz  被传递数据库实体(entity)类型
     * @return QuerWrapper
     */
    QueryWrapper<T> csbrBaseQueryWrapper(Object source, Class<T> clazz);

    /**
     * 有值的字段自动生成查询条件
     * 支持 like(LikeQuery)、比较(CompareQuery) 条件注解
     *
     * @param source 数据传递来源实例
     * @param clazz  被传递数据库实体(entity)类型
     * @return
     */
    LambdaQueryWrapper<T> csbrQueryWrapper(Object source, Class<T> clazz);

    /**
     * 分页查询
     *
     * @param <D>
     * @param dto
     * @param query
     * @return
     */
    <D extends BasePageDTO> PageListVO<T> csbrPageList(D dto, Wrapper<T> query);

    /**
     * 根据guid查询数据是否存在
     *
     * @param guids
     * @param clazz
     * @return
     */
    boolean isExistsData(List<String> guids, Class<T> clazz);

    /**
     * 查询结果按照指定的字段作为key生成map
     *
     * @param queryWrapper 查询条件
     * @param column       key字段
     * @return
     */
    Map<Object, T> csbrMap(Wrapper<T> queryWrapper, SFunction<T, ?> column);
}
