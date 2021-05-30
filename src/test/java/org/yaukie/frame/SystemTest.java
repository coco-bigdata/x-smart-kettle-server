package org.yaukie.frame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.yaukie.auth.config.Md5PasswordEncoder;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author yuenbin
 * @description 系统单元测试
 * @date 2021/5/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SystemTest {

    /**
     * @LocalServerPort 提供了 @Value("${local.server.port}") 的代替
     */
    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate testRestTemplate ;

    @Before
    public void setUp() throws MalformedURLException {
        String url = String.format("http://localhost:%d/",port) ;
        this.base = new URL(url) ;

    }

    @Test
    public void encoder()
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("1"));
    }

    @Test
    public void encoderMd5()
    {
        Md5PasswordEncoder encoder = Md5PasswordEncoder.getInstance();
        encoder.setSalt("yuenbinisagreatman!");
        System.out.println(encoder.encode("1"));
    }

}
