package com.nekoimi.boot;

import com.nekoimi.boot.framework.contract.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
    @Autowired
    private PasswordEncoder encoder;

    @Test
    void contextLoads() {
        String encrypt = encoder.encrypt("Hello Worlddsfsdfsdf4324234@fdfsdf");
        System.out.println(encrypt);
        String decrypt = encoder.decrypt(encrypt);
        System.out.println(decrypt);
    }

}
