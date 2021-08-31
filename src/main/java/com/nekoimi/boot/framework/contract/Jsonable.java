package com.nekoimi.boot.framework.contract;

import com.nekoimi.boot.common.utils.JsonUtils;

/**
 * nekoimi  2021/7/2 下午2:41
 */
public interface Jsonable {
    /**
     * 转成json
     * @return
     */
    default String toJson() {
        return JsonUtils.toJson(this);
    }
}
