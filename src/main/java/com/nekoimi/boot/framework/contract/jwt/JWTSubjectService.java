package com.nekoimi.boot.framework.contract.jwt;


import com.nekoimi.boot.framework.error.exception.FailedToNotFoundErrorException;
import reactor.core.publisher.Mono;

/**
 * @author Nekoimi  2020/5/29 下午2:08
 */
public interface JWTSubjectService {
    Mono<JWTSubject> loadUserById(String subId) throws FailedToNotFoundErrorException;
}
