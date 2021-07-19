package com.nekoimi.boot.common.utils;

import com.nekoimi.boot.framework.contract.Jsonable;
import com.nekoimi.boot.framework.contract.RedisOperator;
import com.nekoimi.boot.framework.error.exception.RedisErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * nekoimi  2020/6/25 上午11:54
 */
@Slf4j
public class RedisUtils<T> implements RedisOperator<T> {
    private final RedisTemplate<String, T> redisTemplate;

    /**
     * @param builder
     */
    private RedisUtils(Builder<T> builder) {
        this.redisTemplate = builder.redisTemplate;
    }

    /**
     * Builder helper
     *
     * @param redisTemplate
     * @param <T>
     * @return
     */
    public static <T> Builder<T> of(RedisTemplate<String, T> redisTemplate) {
        return new Builder<>(redisTemplate);
    }

    /**
     * Builder class
     *
     * @param <T>
     */
    public final static class Builder<T> {
        private final RedisTemplate<String, T> redisTemplate;

        public Builder(RedisTemplate<String, T> redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        public RedisOperator<T> build() {
            return new RedisUtils<>(this);
        }
    }

    @Override
    public void delete(String... keys) {
        if (keys == null || keys.length <= 0) {
            return;
        }
        redisTemplate.delete(Arrays.asList(keys));
    }

    @Override
    public boolean exists(String key) {
        if (key == null) {
            return false;
        }
        Boolean b = redisTemplate.hasKey(key);
        return b != null && b;
    }

    @Override
    public boolean expireSet(String key, long seconds) {
        if (seconds <= 0) {
            return false;
        }
        try {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("expire key (" + key + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean expireSet(String key, LocalDateTime dateTime) {
        try {
            redisTemplate.expireAt(key, Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
            return true;
        } catch (Exception e) {
            log.error("expire key (" + key + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean expireRemove(String key) {
        try {
            Boolean b = redisTemplate.persist(key);
            return b != null && b;
        } catch (Exception e) {
            log.error("persist key (" + key + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public long expireGet(String key) {
        if (key == null) {
            return 0L;
        }
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire == null ? 0L : expire;
    }

    @Override
    public T get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    @Override
    public String getAsString(String key) {
        T t = get(key);
        if (t == null) {
            return "";
        }
        if (t instanceof Jsonable) {
            return ((Jsonable) t).toJson();
        }
        return t.toString();
    }

    @Override
    public boolean set(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("set key (" + key + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean set(String key, T value, long seconds) {
        if (seconds <= 0) {
            return set(key, value);
        }
        try {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("set key (" + key + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public long incr(String key) {
        return incr(key, 1);
    }

    @Override
    public long incr(String key, long step) {
        Long newVal = redisTemplate.opsForValue().increment(key, step);
        if (newVal == null) {
            throw new RedisErrorException("incr key (" + key + "), result is null");
        }
        return newVal;
    }

    @Override
    public long decr(String key) {
        return decr(key, 1);
    }

    @Override
    public long decr(String key, long step) {
        Long newVal = redisTemplate.opsForValue().decrement(key, step);
        if (newVal == null) {
            throw new RedisErrorException("decr key (" + key + "), result is null");
        }
        return newVal;
    }

    @Override
    public long hDelete(String hKey, Object... names) {
        return redisTemplate.opsForHash().delete(hKey, names);
    }

    @Override
    public boolean hExists(String hKey, Object name) {
        return redisTemplate.opsForHash().hasKey(hKey, name);
    }

    @Override
    public Set<Object> hKeys(String hKey) {
        return redisTemplate.opsForHash().keys(hKey);
    }

    @Override
    public Object hGet(String hKey, Object name) {
        return redisTemplate.opsForHash().get(hKey, name);
    }

    @Override
    public Map<Object, Object> hGetAsMap(String hKey) {
        return redisTemplate.opsForHash().entries(hKey);
    }

    @Override
    public boolean hSet(String hKey, Object name, Object value) {
        try {
            redisTemplate.opsForHash().put(hKey, name, value);
            return true;
        } catch (Exception e) {
            log.error("hash set key (" + hKey + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hSet(String hKey, Map<Object, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(hKey, map);
            return true;
        } catch (Exception e) {
            log.error("hash set key (" + hKey + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public long hIncr(String hKey, Object name) {
        return hIncr(hKey, name, 1);
    }

    @Override
    public long hIncr(String hKey, Object name, long step) {
        return redisTemplate.opsForHash().increment(hKey, name, step);
    }

    @Override
    public long hDecr(String hKey, Object name) {
        return hDecr(hKey, name, 1);
    }

    @Override
    public long hDecr(String hKey, Object name, long step) {
        return redisTemplate.opsForHash().increment(hKey, name, -step);
    }

    @Override
    public List<T> lGetAsList(String lKey) {
        return lGetAsList(lKey, 0, -1);
    }

    @Override
    public List<T> lGetAsList(String lKey, long start, long end) {
        return redisTemplate.opsForList().range(lKey, start, end);
    }

    @Override
    public long lGetSize(String lKey) {
        Long size = redisTemplate.opsForList().size(lKey);
        return size == null ? 0L : size;
    }

    @Override
    public T lGetIndex(String lKey, long index) {
        return redisTemplate.opsForList().index(lKey, index);
    }

    @Override
    public boolean lSet(String lKey, T value) {
        try {
            redisTemplate.opsForList().rightPush(lKey, value);
            return true;
        } catch (Exception e) {
            log.error("list set key (" + lKey + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean lSet(String lKey, long index, T value) {
        try {
            redisTemplate.opsForList().set(lKey, index, value);
            return true;
        } catch (Exception e) {
            log.error("list set key (" + lKey + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean lSet(String lKey, List<T> values) {
        try {
            redisTemplate.opsForList().rightPushAll(lKey, values);
            return true;
        } catch (Exception e) {
            log.error("list set key (" + lKey + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public long lDelete(String lKey, T value) {
        return lDelete(lKey, value, 0);
    }

    @Override
    public long lDelete(String lKey, T value, long count) {
        Long remove = redisTemplate.opsForList().remove(lKey, count, value);
        return remove == null ? 0 : remove;
    }

    @Override
    public boolean lTrim(String lKey, long start, long end) {
        try {
            redisTemplate.opsForList().trim(lKey, start, end);
            return true;
        } catch (Exception e) {
            log.error("list trim key (" + lKey + ") fail, " + e.getMessage());
            return false;
        }
    }

    @Override
    public Set<T> sGetAsSet(String sKey) {
        return redisTemplate.opsForSet().members(sKey);
    }

    @Override
    public long sGetSize(String sKey) {
        Long size = redisTemplate.opsForSet().size(sKey);
        return size == null ? 0 : size;
    }

    @Override
    public boolean sExists(String sKey, T value) {
        Boolean b = redisTemplate.opsForSet().isMember(sKey, value);
        return b != null && b;
    }

    @Override
    public long sSet(String sKey, T... values) {
        Long add = redisTemplate.opsForSet().add(sKey, values);
        return add == null ? 0 : add;
    }

    @Override
    public long sDelete(String sKey, Object... values) {
        Long remove = redisTemplate.opsForSet().remove(sKey, values);
        return remove == null ? 0 : remove;
    }
}
