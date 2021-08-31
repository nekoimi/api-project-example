package com.nekoimi.boot.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekoimi.boot.framework.contract.Jsonable;
import com.nekoimi.boot.framework.contract.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * nekoimi  2021/7/2 下午2:55
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisConfiguration {

    public RedisConfiguration() {
        log.debug("[Auto Configuration] RedisConfiguration!");
    }

    @Bean
    @ConditionalOnClass(value = ReactiveRedisOperations.class)
    @ConditionalOnMissingBean(value = RedisOperator.class)
    public RedisOperator<Object> redisOperator(ReactiveRedisOperations<String, Object> redisOperations) {
        return new SimpleRedisOperator<>(redisOperations);
    }

    @Bean
    public ReactiveRedisOperations<String, Object> reactiveRedisOperations(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory,
            ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(objectMapper);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Object>
                builder = RedisSerializationContext.newSerializationContext();
        RedisSerializationContext<String, Object> context = builder
                .string(new StringRedisSerializer())
                .key(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .value(serializer)
                .hashValue(serializer)
                .build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

    /**
     * 对hash类型的数据操作
     */
    @Bean
    public ReactiveHashOperations<String, String, Object> reactiveHashOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     */
    @Bean
    public ReactiveValueOperations<String, Object> reactiveValueOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     */
    @Bean
    public ReactiveListOperations<String, Object> reactiveListOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     */
    @Bean
    public ReactiveSetOperations<String, Object> reactiveSetOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     */
    @Bean
    public ReactiveZSetOperations<String, Object> reactiveZSetOperations(ReactiveRedisOperations<String, Object> redisOperations) {
        return redisOperations.opsForZSet();
    }


    /**
     * 默认的Redis操作实现
     * @param <T>
     */
    public final class SimpleRedisOperator<T> implements RedisOperator<T> {
        private final ReactiveRedisOperations<String, T> redisOperations;

        /**
         * @param redisOperations
         */
        public SimpleRedisOperator(ReactiveRedisOperations<String, T> redisOperations) {
            this.redisOperations = redisOperations;
        }

        @Override
        public Mono<Long> delete(String... keys) {
            if (keys == null || keys.length <= 0) {
                return Mono.just(0L);
            }
            return redisOperations.delete(keys).doOnError(e -> log.error("delete keys ( {} ) fail, {}", String.join(",", keys), e.getMessage()));
        }

        @Override
        public Mono<Boolean> exists(String key) {
            if (key == null) {
                return Mono.just(false);
            }
            return redisOperations.hasKey(key).doOnError(e -> log.error("exists key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<Boolean> expireSet(String key, long seconds) {
            if (seconds <= 0) {
                return Mono.just(false);
            }
            return redisOperations.expire(key, Duration.ofSeconds(seconds)).doOnError(e -> log.error("expire key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<Boolean> expireSet(String key, LocalDateTime dateTime) {
            return redisOperations.expireAt(key, Instant.parse(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))).doOnError(e -> log.error("expire key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<Boolean> expireRemove(String key) {
            return redisOperations.persist(key).doOnError(e -> log.error("persist key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<Long> expireGet(String key) {
            if (key == null) {
                return Mono.just(0L);
            }
            return redisOperations.getExpire(key).map(Duration::toSeconds).doOnError(e -> log.error("get expire key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<T> get(String key) {
            return redisOperations.opsForValue().get(key).doOnError(e -> log.error("get key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<String> getAsString(String key) {
            return get(key).map(t -> {
                if (t == null) return "";
                if (t instanceof Jsonable) return ((Jsonable) t).toJson();
                return t.toString();
            }).doOnError(e -> log.error("get key to string ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<Boolean> set(String key, T value) {
            return redisOperations.opsForValue().set(key, value).doOnError(e -> log.error("set key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<Boolean> set(String key, T value, long seconds) {
            if (seconds <= 0) {
                return set(key, value);
            }
            return redisOperations.opsForValue().set(key, value, Duration.ofSeconds(seconds)).doOnError(e -> log.error("set key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<Long> incr(String key) {
            return incr(key, 1);
        }

        @Override
        public Mono<Long> incr(String key, long step) {
            return redisOperations.opsForValue()
                    .increment(key, step)
                    .doOnSuccess(v -> log.debug("incr key ( {} ) success, value: {}", key, v))
                    .doOnError(e -> log.error("incr key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<Long> decr(String key) {
            return decr(key, 1);
        }

        @Override
        public Mono<Long> decr(String key, long step) {
            return redisOperations.opsForValue()
                    .decrement(key, step)
                    .doOnSuccess(v -> log.debug("decr key ( {} ) success, value: {}", key, v))
                    .doOnError(e -> log.error("decr key ( {} ) fail, {}", key, e.getMessage()));
        }

        @Override
        public Mono<Long> hDelete(String hKey, Object... names) {
            return redisOperations.opsForHash()
                    .remove(hKey, names)
                    .doOnError(e -> log.error("hash delete key ( {} ) => hashKeys ( {} ) fail, {}", hKey, Arrays.toString(names), e.getMessage()));
        }

        @Override
        public Mono<Boolean> hExists(String hKey, Object name) {
            return redisOperations.opsForHash().hasKey(hKey, name).doOnError(e -> log.error("hash exists key ( {} ) => hashKey ( {} ) fail, {}", hKey, name, e.getMessage()));
        }

        @Override
        public Flux<Object> hKeys(String hKey) {
            return redisOperations.opsForHash().keys(hKey).doOnError(e -> log.error("hash keys of key ( {} ) fail, {}", hKey, e.getMessage()));
        }

        @Override
        public Mono<Object> hGet(String hKey, Object name) {
            return redisOperations.opsForHash()
                    .get(hKey, name)
                    .doOnError(e -> log.error("hash get of key ( {} ) => hashKey ( {} ) fail, {}", hKey, name, e.getMessage()));
        }

        @Override
        public Flux<Map.Entry<Object, Object>> hGetAsMap(String hKey) {
            return redisOperations.opsForHash().entries(hKey).doOnError(e -> log.error("hash get map of key ( {} ) fail, {}", hKey, e.getMessage()));
        }

        @Override
        public Mono<Boolean> hSet(String hKey, Object name, Object value) {
            return redisOperations.opsForHash().put(hKey, name, value).doOnError(e -> log.error("hash set key ( {} ) fail, {}", hKey, e.getMessage()));
        }

        @Override
        public Mono<Boolean> hSet(String hKey, Map<Object, Object> map) {
            return redisOperations.opsForHash().putAll(hKey, map).doOnError(e -> log.error("hash set key ( {} ) fail, {}", hKey, e.getMessage()));
        }

        @Override
        public Mono<Long> hIncr(String hKey, Object name) {
            return hIncr(hKey, name, 1);
        }

        @Override
        public Mono<Long> hIncr(String hKey, Object name, long step) {
            return redisOperations.opsForHash().increment(hKey, name, step);
        }

        @Override
        public Mono<Long> hDecr(String hKey, Object name) {
            return hDecr(hKey, name, 1);
        }

        @Override
        public Mono<Long> hDecr(String hKey, Object name, long step) {
            return redisOperations.opsForHash().increment(hKey, name, -step);
        }

        @Override
        public Flux<T> lGetAsList(String lKey) {
            return lGetAsList(lKey, 0, -1);
        }

        @Override
        public Flux<T> lGetAsList(String lKey, long start, long end) {
            return redisOperations.opsForList().range(lKey, start, end);
        }

        @Override
        public Mono<Long> lGetSize(String lKey) {
            return redisOperations.opsForList().size(lKey).defaultIfEmpty(0L);
        }

        @Override
        public Mono<T> lGetIndex(String lKey, long index) {
            return redisOperations.opsForList().index(lKey, index);
        }

        @Override
        public Mono<Boolean> lSet(String lKey, T value) {
            return redisOperations.opsForList().rightPush(lKey, value).map(aLong -> aLong > 0L).doOnError(e -> log.error("list set key ( {} ) fail, {}", lKey, e.getMessage()));
        }

        @Override
        public Mono<Boolean> lSet(String lKey, long index, T value) {
            return redisOperations.opsForList().set(lKey, index, value).doOnError(e -> log.error("list set key ( {} ) fail, {}", lKey, e.getMessage()));
        }

        @Override
        public Mono<Boolean> lSet(String lKey, List<T> values) {
            return redisOperations.opsForList().rightPushAll(lKey, values).map(aLong -> aLong > 0L).doOnError(e -> log.error("list set key ( {} ) fail, {}", lKey, e.getMessage()));
        }

        @Override
        public Mono<Long> lDelete(String lKey, T value) {
            return lDelete(lKey, value, 0);
        }

        @Override
        public Mono<Long> lDelete(String lKey, T value, long count) {
            return redisOperations.opsForList().remove(lKey, count, value);
        }

        @Override
        public Mono<Boolean> lTrim(String lKey, long start, long end) {
            return redisOperations.opsForList().trim(lKey, start, end).doOnError(e -> log.error("list trim key ( {} ) fail, {}", lKey, e.getMessage()));
        }

        @Override
        public Flux<T> sGetAsSet(String sKey) {
            return redisOperations.opsForSet().members(sKey);
        }

        @Override
        public Mono<Long> sGetSize(String sKey) {
            return redisOperations.opsForSet().size(sKey);
        }

        @Override
        public Mono<Boolean> sExists(String sKey, T value) {
            return redisOperations.opsForSet().isMember(sKey, value);
        }

        @Override
        public Mono<Long> sSet(String sKey, T... values) {
            return redisOperations.opsForSet().add(sKey, values);
        }

        @Override
        public Mono<Long> sDelete(String sKey, Object... values) {
            return redisOperations.opsForSet().remove(sKey, values);
        }
    }
}
