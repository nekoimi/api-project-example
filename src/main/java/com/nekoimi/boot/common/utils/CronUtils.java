package com.nekoimi.boot.common.utils;

import com.nekoimi.boot.framework.error.exception.RequestValidationException;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;

/**
 * @author Nekoimi  2020/7/16 2:23
 */
public class CronUtils {

    public static Date checkCron(String cron) {
        CronSequenceGenerator generator = null;
        try {
            generator = new CronSequenceGenerator(cron);
        } catch (Exception ex) {
            throw new RequestValidationException(ex.getMessage());
        }
        return generator.next(new Date());
    }

}
