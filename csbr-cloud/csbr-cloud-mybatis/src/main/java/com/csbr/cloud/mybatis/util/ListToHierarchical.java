package com.csbr.cloud.mybatis.util;

import com.csbr.cloud.common.util.CsbrBeanUtil;
import com.csbr.cloud.mybatis.entity.BaseDO;
import com.csbr.cloud.mybatis.entity.HierarchicalVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: csbr-cloud
 * @description: 列表对象转换成树形结构
 * @author: yio
 * @create: 2020-07-30 17:16
 **/
@Component
public class ListToHierarchical {

    @Autowired
    private CsbrBeanUtil csbrBeanUtil;
    /**
     * 将列表转换为层级关系
     * @param entitys
     * @param leafSignField
     * @param leafSignValue
     * @param clazz
     * @param <T>
     * @param <R>
     * @return
     */
    public <T extends HierarchicalVO,R extends BaseDO> List<T> getHierarchical(List<R> entitys, String leafSignField, String leafSignValue, Class<T > clazz) throws InvocationTargetException, IllegalAccessException {

        Map<String, T> map = new HashMap<>();
        List<T> reusltLst = new ArrayList<>();
        T vo, parentVO;
        String lastParentGuid = "",parentGuid=null;
        for (R entity : entitys) {
            vo = csbrBeanUtil.convert(entity, clazz);
            //移除处理完了的map项
            //反射属性
            PropertyDescriptor pd= BeanUtils.getPropertyDescriptor(entity.getClass(),"parentGuid");
            Method method=pd.getReadMethod();
            parentGuid= (String) method.invoke(entity);
            if (StringUtils.isNotEmpty(lastParentGuid) && !lastParentGuid.equals(parentGuid)) {
                map.remove(lastParentGuid);
            }
            //将根元素添加到结果列表
            if (StringUtils.isEmpty(parentGuid)) {
                reusltLst.add(vo);
            } else {
                lastParentGuid = parentGuid;
            }
            //查找父节点
            if (StringUtils.isNotEmpty(parentGuid)
                    && map.containsKey(parentGuid)) {
                parentVO = map.get(parentGuid);

                if (parentVO.getChildren() == null) {
                    parentVO.setChildren(new ArrayList<>());

                }
                parentVO.getChildren().add(vo);
            }
            //如果菜单时功能按钮，那么不放入到map中
            if(StringUtils.isNotEmpty(leafSignValue))
            {
                pd=BeanUtils.getPropertyDescriptor(entity.getClass(),leafSignField);
                method=pd.getReadMethod();

                if (!leafSignValue.equals(method.invoke(entity))) {
                    pd=BeanUtils.getPropertyDescriptor(entity.getClass(),"guid");
                    method=pd.getReadMethod();
                    map.put((String) method.invoke(entity), vo);
                }
            }

        }
        return reusltLst;
    }

}
