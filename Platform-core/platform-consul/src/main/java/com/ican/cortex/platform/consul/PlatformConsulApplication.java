package com.ican.cortex.platform.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
public class PlatformConsulApplication {


    public static void main(String[] args) {
        SpringApplication.run(PlatformConsulApplication.class, args);
    }


}
