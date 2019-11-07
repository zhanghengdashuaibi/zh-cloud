package com.csbr.cloud.mybatis.entity;

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
    @TableId
    private String guid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标识 0-未删除,1-已删除
     */
    private String delFlag;
}
