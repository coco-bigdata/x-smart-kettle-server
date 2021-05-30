package org.yaukie.frame;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Author: yuenbin
 * @Date :2020/3/1
 * @Time :17:17
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: 注意,如果不需要认证 请添加, SecurityAutoConfiguration.class 排除
 **/
@MapperScan(basePackages = "org.yaukie.**.dao.**")
@SpringBootApplication(scanBasePackages = "org.yaukie.*",
        exclude = {DataSourceAutoConfiguration.class})
 @Slf4j
public class Start extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication();
        //如下设置有可能不生效,,框架做了时区设置
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        SpringApplication.run(Start.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
         return application.sources(Start.class);
    }

}
