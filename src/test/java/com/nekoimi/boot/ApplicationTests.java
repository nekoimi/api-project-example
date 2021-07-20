package com.nekoimi.boot;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.nekoimi.boot.framework.contract.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private IdentifierGenerator idGenerator;

    @Test
    void testEncoder() {
        String encrypt = encoder.encrypt("Hello World");
        System.out.println(encrypt);
        String decrypt = encoder.decrypt(encrypt);
        System.out.println(decrypt);
    }

    @Test
    void testIdGenerator() {
        String uuid = idGenerator.nextUUID(null);
        System.out.println(uuid);
    }

}
