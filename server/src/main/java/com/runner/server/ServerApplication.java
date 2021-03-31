package com.runner.server;

import com.runner.server.service.serviceImpl.InitService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.Resource;

@SpringBootApplication(scanBasePackages = {"com.runner.server"})
@MapperScan(basePackages="com.runner.server.dao.mapper")
@EnableScheduling
@EnableCaching
public class ServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
