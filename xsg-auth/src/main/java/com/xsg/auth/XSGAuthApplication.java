package com.xsg.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.xsg.common.security.annotation.EnableRyFeignClients;

/**
 * 认证授权中心
 * 
 * @author Chenpi
 */
@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class XSGAuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(XSGAuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
