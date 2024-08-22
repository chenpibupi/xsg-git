package com.xsg.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.xsg.common.security.annotation.EnableCustomConfig;
import com.xsg.common.security.annotation.EnableRyFeignClients;
import com.xsg.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 系统模块
 * 
 * @author Chenpi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@MapperScan("com.xsg.system.mapper")
public class XSGSystemApplication
{
    public static void main(String[] args)
    {

        SpringApplication.run(XSGSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
