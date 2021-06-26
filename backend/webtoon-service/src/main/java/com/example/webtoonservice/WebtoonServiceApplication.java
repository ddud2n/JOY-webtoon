package com.example.webtoonservice;

import com.example.webtoonservice.exception.FeignErrorDecoder;
import com.fasterxml.jackson.annotation.JsonFormat;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;


import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {WebtoonServiceApplication.class, Jsr310JpaConverters.class})
@EnableEurekaClient
@EnableFeignClients
public class WebtoonServiceApplication {
    @PostConstruct
    @JsonFormat(timezone = "Asia/Seoul")
    void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
    public static void main(String[] args) {
        SpringApplication.run(WebtoonServiceApplication.class, args);
    }

    @Bean
    public FeignErrorDecoder getFeignErrorDecoder(){
        return new FeignErrorDecoder();
    }
}
