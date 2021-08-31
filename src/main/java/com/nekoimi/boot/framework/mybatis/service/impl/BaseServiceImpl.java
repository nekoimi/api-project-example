package com.nekoimi.boot.framework.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekoimi.boot.framework.error.exception.FailedToCreateErrorException;
import com.nekoimi.boot.framework.error.exception.FailedToNotFoundErrorException;
import com.nekoimi.boot.framework.error.exception.FailedToUpdateErrorException;
import com.nekoimi.boot.framework.http.PagerResult;
import com.nekoimi.boot.framework.mybatis.mapper.BaseMapper;
import com.nekoimi.boot.framework.mybatis.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * nekoimi  2021/6/13 上午12:03
 */
public abstract class BaseServiceImpl<M extends BaseMapper<E>, E> implements BaseService<E> {
    @Autowired
    protected M mapper;
    @Autowired
    protected IdentifierGenerator idGenerator;
    @Autowired
    protected ObjectMapper objectMapper;

    protected TableInfo tableInfo() {
        TableInfo info = TableInfoHelper.getTableInfo(modelClazz());
        if (info == null || StringUtils.isBlank(info.getKeyColumn())) {
            throw new InvalidParameterException("Table " + modelClazz().getName() + " recover info error! ");
        }
        return info;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<E> modelClazz() {
        Class<E> entitiClass = null;
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 1) {
                entitiClass = (Class<E>) actualTypeArguments[1];
            }
        }
        return entitiClass;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsBy(Serializable id) {
        return mapper.selectById(id) != null;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsBy(Wrapper<E> wrapper) {
        return mapper.selectCount(wrapper) > 0;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsBy(Map<String, Object> columnMap) {
        List<E> eList = mapper.selectByMap(columnMap);
        return eList.isEmpty();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Serializable create(E entity) {
        String keyProperty = tableInfo().getKeyProperty();
        Object keyValue = ReflectionKit.getFieldValue(entity, keyProperty);
        if (keyValue == null) {
            keyValue = idGenerator.nextUUID(entity);
            Map<String, Field> fieldMap = ReflectionKit.getFieldMap(entity.getClass());
            Field field = fieldMap.get(keyProperty);
            field.setAccessible(true);
            try {
                field.set(entity, keyValue);
            } catch (IllegalAccessException ex) {
                throw new FailedToCreateErrorException(ex.getMessage());
            }
        }
        int insert;
        try {
            insert = mapper.insert(entity);
        } catch (DuplicateKeyException ex) {
            throw new FailedToCreateErrorException(
                    String.format("duplicate primary key (%s) for entity", keyValue)
            );
        }
        if (!SqlHelper.retBool(insert)) {
            throw new FailedToCreateErrorException();
        }
        return (Serializable) keyValue;
    }

    @Override
    public Serializable create(Map<String, Object> map) {
        Class<E> modelClazz = modelClazz();
        E e = null;
        try {
            e = modelClazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException rex) {
            throw new FailedToCreateErrorException(rex.getMessage());
        }
        // fixme 这里返回的数据表字段不包含主键
        List<TableFieldInfo> fieldList = tableInfo().getFieldList();
        try {
            for (TableFieldInfo fieldInfo : fieldList) {
                Field field = fieldInfo.getField();
                String fieldName = field.getName();
                if (map.containsKey(fieldName)) {
                    field.setAccessible(true);
                    Object value = map.get(fieldName);
                    if (value instanceof Map<?, ?> ||
                            value instanceof Collection<?>) {
                        Class<?> type = field.getType();
                        try {
                            String json = objectMapper.writeValueAsString(value);
                            value = objectMapper.readValue(json, type);
                        } catch (JsonProcessingException jex) {
                            throw new FailedToCreateErrorException(jex.getMessage());
                        }
                    }
                    field.set(e, value);
                }
            }

            String keyProperty = tableInfo().getKeyProperty();
            if (map.containsKey(keyProperty)) {
                Field keyField = modelClazz.getDeclaredField(keyProperty);
                keyField.setAccessible(true);
                keyField.set(e, map.get(keyProperty));
            }
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            throw new FailedToCreateErrorException(ex.getMessage());
        }
        return create(e);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean createBatch(List<E> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return false; // Ignore
        }
        // 获取Insert statement
        String statement = SqlHelper.table(modelClazz()).getSqlStatement(SqlMethod.INSERT_ONE.getMethod());
        try (SqlSession sqlSessionBatch = SqlHelper.sqlSessionBatch(modelClazz())) {
            int i = 0;
            for (E e : entityList) {
                sqlSessionBatch.insert(statement, e);
                // fixme 避免超过最大批量操作数量
                // 但是这里可能导致事务无法回滚
                if (i > 0 && i % maxBatchSize == 0) {
                    sqlSessionBatch.flushStatements();
                }
                i++;
            }
            sqlSessionBatch.flushStatements();
        }
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public E getBy(Serializable id) {
        if (id == null) return null;
        return mapper.selectById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public E getBy(Wrapper<E> wrapper) {
        return mapper.selectOne(wrapper);
    }

    @Transactional(readOnly = true)
    @Override
    public E getBy(Map<String, Object> columnMap) {
        List<E> eList = mapper.selectByMap(columnMap);
        if (eList == null || eList.isEmpty()) {
            return null;
        }
        for (E e : eList) {
            return e;
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public E getByOrFail(Serializable id) {
        E e = getBy(id);
        if (e == null) {
            throw new FailedToNotFoundErrorException(
                    String.format("No corresponding resource found according to ID (%s), please check the data and try again!", id)
            );
        }
        return e;
    }

    @Transactional(readOnly = true)
    @Override
    public E getByOrFail(Wrapper<E> wrapper) {
        E e = getBy(wrapper);
        if (e == null) {
            throw new FailedToNotFoundErrorException();
        }
        return e;
    }

    @Transactional(readOnly = true)
    @Override
    public E getByOrFail(Map<String, Object> columnMap) {
        E e = getBy(columnMap);
        if (e == null) {
            throw new FailedToNotFoundErrorException();
        }
        return e;
    }

    @Transactional(readOnly = true)
    @Override
    public List<E> findAll() {
        return mapper.selectList(Wrappers.emptyWrapper());
    }

    @Transactional(readOnly = true)
    @Override
    public List<E> findAll(Wrapper<E> wrapper) {
        return mapper.selectList(wrapper);
    }

    @Transactional(readOnly = true)
    @Override
    public List<E> findAll(Map<String, Object> columnMap) {
        return mapper.selectByMap(columnMap);
    }

    @Transactional(readOnly = true)
    @Override
    public PagerResult<E> listPaginator(IPage<E> page, Wrapper<E> wrapper) {
        return new PagerResult<>(mapper.selectPage(page, wrapper));
    }

    @Transactional(readOnly = true)
    @Override
    public PagerResult<Map<String, Object>> listMapPaginator(IPage<Map<String, Object>> page, Wrapper<E> wrapper) {
        return new PagerResult<>(mapper.selectMapsPage(page, wrapper));
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean update(E entity) {
        return SqlHelper.retBool(mapper.updateById(entity));
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean update(E entity, Wrapper<E> wrapper) {
        return SqlHelper.retBool(mapper.update(entity, wrapper));
    }

    @Override
    public boolean update(Serializable id, Map<String, Object> map) {
        E e = getByOrFail(id);
        List<TableFieldInfo> fieldList = tableInfo().getFieldList();
        try {
            for (TableFieldInfo fieldInfo : fieldList) {
                Field field = fieldInfo.getField();
                String fieldName = field.getName();
                if (map.containsKey(fieldName)) {
                    field.setAccessible(true);
                    field.set(e, map.get(fieldName));
                }
            }
        } catch (IllegalAccessException ex) {
            throw new FailedToUpdateErrorException(ex.getMessage());
        }
        return update(e);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void removeBy(Serializable id) {
        mapper.deleteById(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void removeBy(List<? extends Serializable> idList) {
        mapper.deleteBatchIds(idList);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void removeBy(Wrapper<E> wrapper) {
        mapper.delete(wrapper);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void removeBy(Map<String, Object> columnMap) {
        mapper.deleteByMap(columnMap);
    }
}
