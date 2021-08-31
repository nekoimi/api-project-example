package com.nekoimi.boot.framework.http;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * nekoimi  2021/7/28 上午11:33
 */
@Getter
@Setter
public class PagerResult<T> {
    private final long total;
    private final long lastPage;
    private final long currentPage;
    private final long perPage;
    private final List<T> list;

    public PagerResult(IPage<T> page) {
        this.total = page.getTotal();
        this.lastPage = page.getPages();
        this.currentPage = page.getCurrent();
        this.perPage = page.getSize();
        this.list = page.getRecords();
    }
}
