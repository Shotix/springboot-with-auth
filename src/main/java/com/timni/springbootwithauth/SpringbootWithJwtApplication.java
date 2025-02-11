package com.timni.springbootwithauth;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@Slf4j
@EnableAsync
@EnableCaching
@ConfigurationPropertiesScan
@SpringBootApplication
public class SpringbootWithJwtApplication {

    public static void main(String[] args) throws Exception {
        final Runtime r = Runtime.getRuntime();

        log.info("[APP] Active processors: {}", r.availableProcessors());
        log.info("[APP] Total memory: {}", r.totalMemory());
        log.info("[APP] Free memory: {}", r.freeMemory());
        log.info("[APP] Max memory: {}", r.maxMemory());
        SpringApplication.run(SpringbootWithJwtApplication.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
    }

}
