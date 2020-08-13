package com.csbr.cloud.mybatis.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * @program: user-auth-service
 * @description: 分页数据视图对象
 * @author: Huanglh
 * @create: 2020-07-13 15:41
 **/
@Data
public class PageListVO<T> {
    private Long totalRows;
    private Long totalPages;
    private Long pageSize;
    private Long pageIndex;
    private List<T> records;

    public PageListVO build(IPage<T> page) {
        this.setTotalRows(page.getTotal());
        this.setTotalPages(page.getPages());
        this.setPageIndex(page.getCurrent());
        this.setPageSize(page.getSize());
        this.setRecords(page.getRecords());

        return this;
    }
}
