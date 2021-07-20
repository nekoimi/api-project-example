package com.nekoimi.boot.framework.holder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/7/20 下午2:18
 */
@Slf4j
@Component
public class ContextHolder implements ApplicationContextAware {
    private static ApplicationContext ctx;

    public static ApplicationContext getCtx() {
        return ctx;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ContextHolder.ctx = context;

        log.debug("Application context saved!");
    }
}
