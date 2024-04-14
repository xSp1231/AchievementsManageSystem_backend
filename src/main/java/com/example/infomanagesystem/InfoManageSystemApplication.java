package com.example.infomanagesystem;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching  //启用缓存注解
public class InfoManageSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfoManageSystemApplication.class, args);
    }

}
