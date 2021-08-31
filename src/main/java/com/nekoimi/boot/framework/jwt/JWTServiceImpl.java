package com.nekoimi.boot.framework.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import com.nekoimi.boot.framework.config.properties.JWTProperties;
import com.nekoimi.boot.framework.contract.jwt.JWTService;
import com.nekoimi.boot.framework.contract.jwt.JWTStorage;
import com.nekoimi.boot.framework.contract.jwt.JWTSubject;
import com.nekoimi.boot.framework.contract.jwt.JWTSubjectService;
import com.nekoimi.boot.framework.error.exception.token.TokenCannotBeRefreshException;
import com.nekoimi.boot.framework.error.exception.token.TokenHasBeenBlackListedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nekoimi  2020/5/28 0:14
 */
@Slf4j
@Component
public class JWTServiceImpl implements JWTService {
    private final String issuer;
    private final Algorithm algorithm;
    private final JWTProperties jwtProperties;
    private final JWTStorage jwtStorage;

    public JWTServiceImpl(JWTProperties jwtProperties,
                          JWTStorage jwtStorage) {
        this.issuer = getClass().toString();
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        this.jwtProperties = jwtProperties;
        this.jwtStorage = jwtStorage;
    }

    @Override
    public Mono<String> encode(JWTSubject sub) {
        return Mono.defer(() -> Mono.just(new Date()))
                .flatMap(date -> Mono.just(JWT.create())
                        .map(builder -> builder.withIssuedAt(date))
                        .map(builder -> builder.withExpiresAt(DateUtils.addMinutes(date, jwtProperties.getTtl())))
                        .map(builder -> builder.withSubject(sub.getJWTIdentifier())))
                .flatMap(builder -> Mono.just(sub.getJWTCustomClaims())
                        .filter(Objects::nonNull)
                        .filter(map -> !map.isEmpty())
                        .map(map -> {
                            for (Map.Entry<String, String> e : map.entrySet()) {
                                builder.withClaim(e.getKey(), e.getValue());
                            }
                            return builder;
                        })
                ).map(builder -> builder.sign(algorithm))
                .map(token -> {
                    // 设置Token的刷新期限
                    jwtStorage.setRefresh(token, jwtProperties.getRefreshTtl());
                    return token;
                });
    }

    @Override
    public Mono<String> decode(String token) {
        return parse(token, true).map(Payload::getSubject);
    }

    @Override
    public synchronized Mono<String> refresh(JWTSubjectService jwtSubjectService, String token) {
        // todo 这里需要检查这个token是否已经被刷新过 旧Token已经被刷新过就不需要在刷新了
        String refreshedToken = jwtStorage.getRefreshed(token).block();
        if (StringUtils.isNotBlank(refreshedToken)) {
            jwtStorage.black(token);
            return Mono.just(refreshedToken);
        }


        String refreshToken = jwtStorage.getRefresh(token).block();
        if (StringUtils.isBlank(refreshToken)) {
            // 当前Token已经超过刷新期限了
            return Mono.error(new TokenCannotBeRefreshException());
        }

        return parse(token, false)
                .map(Payload::getSubject)
                .flatMap(jwtSubjectService::loadUserById)
                .flatMap(jwtSubject -> encode(jwtSubject).map(newToken -> {
                    // todo 将旧Token设置为已经刷新过了
                    jwtStorage.setRefreshed(token, newToken, jwtProperties.getRefreshConcurrentTtl() <= jwtProperties.getTtl() ? jwtProperties.getRefreshConcurrentTtl() : jwtProperties.getTtl());
                    jwtStorage.black(token);
                    return newToken;
                }));
    }

    @Override
    public Mono<DecodedJWT> parse(String token, boolean isCheck) {
        return Mono.just(token)
                .flatMap(jwtStorage::isBlack)
                .map(b -> b ? Mono.empty() : Mono.just(token))
                .switchIfEmpty(Mono.just(Mono.defer(() -> Mono.just(token))
                        .flatMap(s -> jwtStorage.getRefreshed(s)
                                .flatMap(result -> Mono.just(result)
                                        .flatMap(rt -> StringUtils.isNotBlank(rt) ?
                                                Mono.just(rt) :
                                                Mono.error(new TokenHasBeenBlackListedException())))
                        )))
                .flatMap(mono -> mono.map(Object::toString).flatMap(s -> {
                    DecodedJWT decodedJWT = null;
                    if (isCheck) {
                        decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(s);
                    } else {
                        decodedJWT = JWT.decode(s);
                    }
                    return Mono.just(decodedJWT);
                }));
    }

    @Override
    public Mono<Void> flush() {
        return jwtStorage.flush();
    }

}
