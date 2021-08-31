package com.nekoimi.boot.framework.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nekoimi.boot.framework.http.PagerResult;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * nekoimi  2021/6/13 上午12:03
 *
 * Service基本CRUD方法封装
 */
public interface BaseService<E> {

    int maxBatchSize = 1024;

    /**
     * @return 返回当前实体类
     */
    Class<E> modelClazz();

    /**
     * 根据ID查询是否存在
     * @param id 表主键
     * @return bool
     */
    boolean existsBy(Serializable id);

    /**
     * 根据查询条件判断是否存在
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return bool
     */
    boolean existsBy(@Param(Constants.WRAPPER) Wrapper<E> wrapper);

    /**
     * 根据查询条件判断是否存在
     * @param columnMap 表字段 map 对象
     * @return bool
     */
    boolean existsBy(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 创建一条记录
     * @param entity 实体对象
     */
    Serializable create(E entity);

    /**
     * 创建一条记录
     * @param map 实体属性参数Map
     */
    Serializable create(Map<String, Object> map);

    /**
     * 批量创建
     * @param entityList 实体对象列表
     */
    boolean createBatch(List<E> entityList);

    /**
     * 根据ID获取实体
     * @param id 表主键
     * @return E
     */
    E getBy(Serializable id);

    /**
     * 根据查询条件获取实体
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return E
     */
    E getBy(@Param(Constants.WRAPPER) Wrapper<E> wrapper);

    /**
     * 根据查询条件获取实体
     * @param columnMap 表字段 map 对象
     * @return E
     */
    E getBy(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 根据ID获取实体，不存在抛出异常
     * @param id 表主键
     * @return E
     */
    E getByOrFail(Serializable id);

    /**
     * 根据查询条件获取实体，不存在抛出异常
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return E
     */
    E getByOrFail(@Param(Constants.WRAPPER) Wrapper<E> wrapper);

    /**
     * 根据查询条件获取实体，不存在抛出异常
     * @param columnMap 表字段 map 对象
     * @return E
     */
    E getByOrFail(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 获取所有
     * @return 实体列表
     */
    List<E> findAll();

    /**
     * 根据查询条件获取所有
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return 实体列表
     */
    List<E> findAll(@Param(Constants.WRAPPER) Wrapper<E> wrapper);

    /**
     * 根据查询条件获取所有
     * @param columnMap 表字段 map 对象
     * @return 实体列表
     */
    List<E> findAll(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页显示数量
     * @return pageList
     */
    default PagerResult<E> listPaginator(int page, int pageSize) {
        return listPaginator(new Page<>(page, pageSize));
    }

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页显示数量
     * @return pageList
     */
    default PagerResult<E> listPaginator(int page, int pageSize, @Param(Constants.WRAPPER) Wrapper<E> wrapper) {
        return listPaginator(new Page<>(page, pageSize), wrapper);
    }

    /**
     * 分页查询
     * @param page IPage对象
     * @return pageList
     */
    default PagerResult<E> listPaginator(IPage<E> page) {
        return listPaginator(page, Wrappers.emptyWrapper());
    }

    /**
     * 分页查询
     * @param page iPage对象
     * @param wrapper 查询对象
     * @return pageList
     */
    PagerResult<E> listPaginator(IPage<E> page, @Param(Constants.WRAPPER) Wrapper<E> wrapper);


    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页显示数量
     * @return pageList
     */
    default PagerResult<Map<String, Object>> listMapPaginator(int page, int pageSize) {
        return listMapPaginator(new Page<>(page, pageSize));
    }

    /**
     * 分页查询
     * @param page 页码
     * @param pageSize 每页显示数量
     * @return pageList
     */
    default PagerResult<Map<String, Object>> listMapPaginator(int page, int pageSize, @Param(Constants.WRAPPER) Wrapper<E> wrapper) {
        return listMapPaginator(new Page<>(page, pageSize), wrapper);
    }

    /**
     * 分页查询
     * @param page IPage对象
     * @return pageList
     */
    default PagerResult<Map<String, Object>> listMapPaginator(IPage<Map<String, Object>> page) {
        return listMapPaginator(page, Wrappers.emptyWrapper());
    }

    /**
     * 分页查询
     * @param page iPage对象
     * @param wrapper 查询对象
     * @return pageList
     */
    PagerResult<Map<String, Object>> listMapPaginator(IPage<Map<String, Object>> page, @Param(Constants.WRAPPER) Wrapper<E> wrapper);


    /**
     * 根据ID更新
     * @param entity 实体
     * @return bool
     */
    boolean update(E entity);

    /**
     * 根据条件更新
     * @param entity 实体
     * @param wrapper 查询对象
     * @return bool
     */
    boolean update(E entity, Wrapper<E> wrapper);

    /**
     * 根据ID更新
     * @param map 更新参数Map
     * @return bool
     */
    boolean update(Serializable id, Map<String, Object> map);

    /**
     * 根据ID删除
     * @param id 表主键
     */
    void removeBy(Serializable id);

    /**
     * 根据ID批量删除
     * @param idList 主键列表
     */
    void removeBy(List<? extends Serializable> idList);

    /**
     * 根据条件删除
     * @param wrapper 查询对象
     */
    void removeBy(Wrapper<E> wrapper);

    /**
     * 根据条件删除
     * @param columnMap 查询条件
     */
    void removeBy(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);
}
