package com.hnzz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @PackageName:com.zzkj.application
 * @ClassName:GroupApplication
 * @Author 冼大丰
 * @Date 2023/1/4 10:20
 * @Version 1.0
 **/
@Slf4j
@SpringBootApplication(scanBasePackages = "com.hnzz")
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class,args);
        log.info("============启动成功==============");
    }
}
