package com.csbr.cloud.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据库对象基类
 *
 * @author arthas on 2019/10/14
 */
@Data
public class BaseDO implements Serializable {
    /**
     * 主键ID,通过UUID生成
     */
    @TableId(type = IdType.UUID)
//    @TableField(fill = FieldFill.INSERT)
    private String guid;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime=new Date();

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,update="NOW()")
    private Date updateTime=new Date();

    /**
     * 删除标识 0-未删除,1-已删除
     */
    @TableField(exist=false)
    private String delFlag;

    /** 是否删除(Y 是；N 否 默认 N) */
    @TableField(exist=false)
    private String isDeleted;
}
