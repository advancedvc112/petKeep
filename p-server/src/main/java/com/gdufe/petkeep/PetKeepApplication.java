package com.gdufe.petkeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 宠物养护系统 — 启动类
 *
 * @SpringBootApplication 等价于：
 *   @SpringBootConfiguration  — 标记为 Spring 配置类
 *   @EnableAutoConfiguration  — 开启 Spring Boot 自动装配
 *   @ComponentScan            — 扫描当前包及子包下的所有 Bean
 */
@SpringBootApplication
public class PetKeepApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetKeepApplication.class, args);
    }
}
