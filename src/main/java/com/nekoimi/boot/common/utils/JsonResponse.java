package com.nekoimi.boot.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nekoimi.boot.framework.contract.Jsonable;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

/**
 * nekoimi  2021/6/28 下午5:54
 */
@Getter
public class JsonResponse implements Jsonable {
    private final int code;
    private final String message;
    private final String method;
    private final String path;
    private final long timestamp;
    private final Object data;

    private JsonResponse(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.method = builder.method;
        this.path = builder.path;
        this.data = builder.data;
        this.timestamp = builder.timestamp;
    }

    public static Builder ok() {
        return new Builder();
    }

    public static Builder ok(Object data) {
        return new Builder(data);
    }

    public static Builder ok(IPage<?> page) {
        return new Builder(page);
    }

    @Override
    public String toJson() {
        return JsonUtils.of(this).toJson();
    }

    public final static class Builder {
        private int code = 0;
        private String message = "ok";
        private String method;
        private String path;
        private Object data = new HashMap<>();
        private final long timestamp = System.currentTimeMillis();

        private Builder() {
        }

        private Builder(Object data) {
            this.data = data;
        }

        private Builder(IPage<?> data) {
            this.data = new PaginatorResult<>(data);
        }

        public Builder withCode(int code) {
            this.code = code;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public JsonResponse build() {
            return new JsonResponse(this);
        }
    }

    @Getter
    public static class PaginatorResult<T> {
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
}
