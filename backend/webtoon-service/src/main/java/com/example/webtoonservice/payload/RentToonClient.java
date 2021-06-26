package com.example.webtoonservice.payload;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "CASH-SERVICE", path = "cash-service/CheckRentEpi/")
public interface RentToonClient {
    @GetMapping(path = "{Id}")
    String getRentToonResult(@PathVariable("Id") String Id, @RequestHeader("Authorization") String token);
}
