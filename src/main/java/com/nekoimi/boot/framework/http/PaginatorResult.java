package com.nekoimi.boot.framework.http;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;

import java.util.List;

/**
 * nekoimi  2021/6/28 上午10:43
 */
@Getter
public class PaginatorResult<T> {
    private final long total;
    private final long lastPage;
    private final long currentPage;
    private final long perPage;
    private final List<T> list;

    public PaginatorResult(IPage<T> page) {
        this.total = page.getTotal();
        this.lastPage = page.getPages();
        this.currentPage = page.getCurrent();
        this.perPage = page.getSize();
        this.list = page.getRecords();
    }
}
