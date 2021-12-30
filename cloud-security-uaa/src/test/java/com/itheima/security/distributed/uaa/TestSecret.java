package com.itheima.security.distributed.uaa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName com.itheima.security.distributed.uaa.TestSecret
 * @Description TODO
 * @Author LHQ
 * @Date 2021/11/24 18:07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSecret {

    @Test
    public void testD(){
        String secret = new BCryptPasswordEncoder().encode("secret");
        System.out.println(secret);
    }

}
