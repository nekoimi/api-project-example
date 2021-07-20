package com.nekoimi.boot.framework.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nekoimi.boot.framework.configuration.properties.JWTProperties;
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

    public JWTServiceImpl(JWTProperties jwtProperties, JWTStorage jwtStorage) {
        this.issuer = getClass().toString();
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        this.jwtProperties = jwtProperties;
        this.jwtStorage = jwtStorage;
    }

    @Override
    public String encode(JWTSubject sub) {
        Date now = new Date();

        JWTCreator.Builder builder = JWT.create();

        builder = builder.withIssuer(getClass().toString());
        builder = builder.withIssuedAt(now);
        builder = builder.withExpiresAt(DateUtils.addMinutes(now, jwtProperties.getTtl()));
        builder = builder.withSubject(sub.getJWTIdentifier());

        Map<String, String> jWTCustomClaims = sub.getJWTCustomClaims();
        if (Objects.nonNull(jWTCustomClaims) && !jWTCustomClaims.isEmpty()) {
            for (Map.Entry<String, String> entry : jWTCustomClaims.entrySet()) {
                builder = builder.withClaim(entry.getKey(), entry.getValue());
            }
        }

        String token = builder.sign(algorithm);

        // 设置Token的刷新期限
        jwtStorage.setRefresh(token, jwtProperties.getRefreshTtl());

        return token;
    }

    @Override
    public String decode(String token) {
        return parse(token, true).getSubject();
    }

    @Override
    public synchronized String refresh(String token, JWTSubjectService jwtSubjectService) {
        // todo 这里需要检查这个token是否已经被刷新过 旧Token已经被刷新过就不需要在刷新了
        String refreshedToken = jwtStorage.getRefreshed(token);
        if (StringUtils.isNotBlank(refreshedToken)) {
            jwtStorage.black(token);
            return refreshedToken;
        }


        String refreshToken = jwtStorage.getRefresh(token);
        if (StringUtils.isBlank(refreshToken)) {
            // 当前Token已经超过刷新期限了
            throw new TokenCannotBeRefreshException();
        }

        JWTSubject jwtSubject = jwtSubjectService.loadUserById(parse(token, false).getSubject());

        String newToken = encode(jwtSubject);
        // todo 将旧Token设置为已经刷新过了
        jwtStorage.setRefreshed(token, newToken, jwtProperties.getRefreshConcurrentTtl() <= jwtProperties.getTtl() ? jwtProperties.getRefreshConcurrentTtl() : jwtProperties.getTtl());

        jwtStorage.black(token);

        return newToken;
    }

    @Override
    public DecodedJWT parse(String token, boolean isCheck) {
        if (jwtStorage.isBlack(token)) {
            String refreshedToken = jwtStorage.getRefreshed(token);
            if (StringUtils.isBlank(refreshedToken)) {
                throw new TokenHasBeenBlackListedException();
            }

            token = refreshedToken;
        }

        DecodedJWT decodedJWT = null;

        if (isCheck) {
            decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        } else {
            decodedJWT = JWT.decode(token);
        }

        return decodedJWT;
    }

    @Override
    public void flush() {
        jwtStorage.flush();
    }

}
