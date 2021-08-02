package com.nekoimi.boot.framework.configuration;

import com.nekoimi.boot.framework.configuration.properties.AppProperties;
import com.nekoimi.boot.framework.contract.PasswordEncoder;
import com.nekoimi.boot.framework.contract.jwt.JWTSubjectService;
import com.nekoimi.boot.framework.error.exception.FailedToEncoderErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TimeZone;

/**
 * nekoimi  2021/7/20 上午9:51
 */
@Slf4j
@Configuration
public class AppConfiguration {
    public AppConfiguration() {
        log.debug("[Auto Configuration] AppConfiguration!");
    }

    @PostConstruct
    public void setDefault() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));

        log.debug("Set default success!");
    }

    @Bean
    @ConditionalOnMissingBean(value = JWTSubjectService.class, search = SearchStrategy.CURRENT)
    public JWTSubjectService sampleJWTSubjectService() {
        return subId -> null;
    }

    @Bean
    @ConditionalOnMissingBean(value = PasswordEncoder.class, search = SearchStrategy.CURRENT)
    public PasswordEncoder samplePasswordEncoder(AppProperties properties) {
        return new SamplePasswordEncoder(properties);
    }

    public static final class SamplePasswordEncoder implements PasswordEncoder {
        private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
        private final Cipher cipher;
        private final SecretKeySpec keySpec;
        private final IvParameterSpec ivSpec;

        public SamplePasswordEncoder(AppProperties properties) {
            try {
                Cipher.getMaxAllowedKeyLength(ALGORITHM);
                cipher = Cipher.getInstance(ALGORITHM);
                String key = properties.getAppKey();
                if (StringUtils.isBlank(key)) {
                    throw new FailedToEncoderErrorException("Key is empty");
                }
                byte[] bytes = DigestUtils.md5(key.getBytes());
                int ignore = bytes.length % 16;
                byte[] kk = new byte[16];
                int offset = (bytes.length - ignore) / 16;
                int j = 0;
                for (int i = 0; i < bytes.length - ignore; i += offset) {
                    kk[j] = bytes[i];
                    j++;
                }
                keySpec = new SecretKeySpec(kk, "AES");
                ivSpec = new IvParameterSpec(kk);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                log.error(e.getMessage());

                throw new FailedToEncoderErrorException(e.getMessage());
            }
        }

        @Override
        public String encrypt(String raw) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
                byte[] bytes = cipher.doFinal(raw.getBytes());
                return Base64Utils.encodeToString(bytes);
            } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                log.error(e.getMessage());

                throw new FailedToEncoderErrorException(e.getMessage());
            }
        }

        @Override
        public String decrypt(String code) {
            try {
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
                byte[] bytes = cipher.doFinal(Base64Utils.decodeFromString(code));
                return new String(bytes);
            } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
                log.error(e.getMessage());

                throw new FailedToEncoderErrorException(e.getMessage());
            }
        }

        @Override
        public boolean matches(String raw, String code) {
            return encrypt(raw).equals(code);
        }
    }

}
