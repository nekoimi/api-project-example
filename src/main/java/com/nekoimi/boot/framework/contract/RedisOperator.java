package com.nekoimi.boot.framework.contract;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * nekoimi  2021/7/2 下午2:41
 *
 * 全局Redis操作接口
 */
public interface RedisOperator<T> {
    /**
     * 删除缓存
     * @param keys
     */
    Mono<Long> delete(String...keys);

    /**
     * 判断缓存是否存在
     * @param key
     * @return
     */
    Mono<Boolean> exists(String key);

    /**
     * 设置缓存过期时间
     * @param key
     * @param seconds
     * @return
     */
    Mono<Boolean> expireSet(String key, long seconds);

    /**
     * 设置缓存过期时间
     * @param key
     * @param dateTime
     * @return
     */
    Mono<Boolean> expireSet(String key, LocalDateTime dateTime);

    /**
     * 移除缓存过期时间
     * @param key
     * @return
     */
    Mono<Boolean> expireRemove(String key);

    /**
     * 获取缓存过期时间
     * @param key
     * @return 返回剩余秒数
     */
    Mono<Long> expireGet(String key);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    Mono<T> get(String key);

    /**
     * 获取字符串缓存
     * @param key
     * @return
     */
    Mono<String> getAsString(String key);

    /**
     * 设置缓存
     * @param key
     * @param value
     * @return
     */
    Mono<Boolean> set(String key, T value);

    /**
     * 设置缓存，并设置过期时间
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    Mono<Boolean> set(String key, T value, long seconds);

    /**
     * 对指定缓存加一
     * @param key
     * @return
     */
    Mono<Long> incr(String key);

    /**
     * @param key
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> incr(String key, long step);

    /**
     * 对指定缓存减一
     * @param key
     * @return
     */
    Mono<Long> decr(String key);

    /**
     * @param key
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> decr(String key, long step);

    /**
     * Hash(hKey, name, value)
     *
     * Hash 删除
     * @param hKey
     * @param names
     * @return
     */
    Mono<Long> hDelete(String hKey, Object...names);

    /**
     * 判断hash中是否存在name
     * @param hKey
     * @param name
     * @return
     */
    Mono<Boolean> hExists(String hKey, Object name);

    /**
     * 获取哈希表中的所有域（field）
     * @param hKey
     * @return
     */
    Flux<Object> hKeys(String hKey);

    /**
     * 获取hash指定缓存
     * @param hKey
     * @param name
     * @return
     */
    Mono<Object> hGet(String hKey, Object name);

    /**
     * 获取指定hash的全部 k => v 值
     * @param hKey
     * @return
     */
    Flux<Map.Entry<Object, Object>> hGetAsMap(String hKey);

    /**
     * 设置hash缓存
     * @param hKey
     * @param name
     * @param value
     * @return
     */
    Mono<Boolean> hSet(String hKey, Object name, Object value);

    /**
     * 设置hash缓存，参数为map
     * @param hKey
     * @param map
     * @return
     */
    Mono<Boolean> hSet(String hKey, Map<Object, Object> map);

    /**
     * 对指定缓存加一
     * @param hKey
     * @param name
     * @return
     */
    Mono<Long> hIncr(String hKey, Object name);

    /**
     * @param hKey
     * @param name
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> hIncr(String hKey, Object name, long step);

    /**
     * 对指定缓存减一
     * @param hKey
     * @param name
     * @return
     */
    Mono<Long> hDecr(String hKey, Object name);

    /**
     * @param hKey
     * @param name
     * @param step
     * @return 操作之后的值
     */
    Mono<Long> hDecr(String hKey, Object name, long step);

    /**
     * 获取全部列表
     * @param lKey
     * @return
     */
    Flux<T> lGetAsList(String lKey);

    /**
     * 获取指定返回的列表
     * @param lKey
     * @param start
     * @param end
     * @return
     */
    Flux<T> lGetAsList(String lKey, long start, long end);

    /**
     * 获取缓存列表长度
     * @param lKey
     * @return
     */
    Mono<Long> lGetSize(String lKey);

    /**
     * 获取列表中指定index的缓存
     * @param lKey
     * @param index 索引 index >= 0 时， 0 首个元素，依次类推；index < 0 时，-1 最后一个元素，-2 倒数第二个元素，依次类推
     * @return
     */
    Mono<T> lGetIndex(String lKey, long index);

    /**
     * 列表后面追加
     * @param lKey
     * @param value
     * @return
     */
    Mono<Boolean> lSet(String lKey, T value);

    /**
     * 列表指定位置插入
     * @param lKey
     * @param index
     * @param value
     * @return
     */
    Mono<Boolean> lSet(String lKey, long index, T value);

    /**
     * 缓存list
     * @param lKey
     * @param values
     * @return
     */
    Mono<Boolean> lSet(String lKey, List<T> values);

    /**
     * 移除列表中所有和value值相同的缓存
     * @param lKey
     * @param value
     * @return
     */
    Mono<Long> lDelete(String lKey, T value);

    /**
     * 移除列表中和value相同的缓存
     * @param lKey
     * @param value
     * @param count
     * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT
     * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值
     * count = 0 : 移除表中所有与 VALUE 相等的值
     * @return
     */
    Mono<Long> lDelete(String lKey, T value, long count);

    /**
     * 移除列表中除了 start ~ end 之间的其他数据
     * @param lKey
     * @param start
     * @param end
     * @return
     */
    Mono<Boolean> lTrim(String lKey, long start, long end);

    /**
     * 获取全部set
     * @param sKey
     * @return
     */
    Flux<T> sGetAsSet(String sKey);

    /**
     * 获取set中缓存数量
     * @param sKey
     * @return
     */
    Mono<Long> sGetSize(String sKey);

    /**
     * 判断Set中是否存在 value
     * @param sKey
     * @param value
     * @return
     */
    Mono<Boolean> sExists(String sKey, T value);

    /**
     * 缓存set
     * @param sKey
     * @param values
     * @return
     */
    Mono<Long> sSet(String sKey, T...values);

    /**
     * 删除集合中缓存
     * @param sKey
     * @param values
     * @return
     */
    Mono<Long> sDelete(String sKey, Object...values);
}
