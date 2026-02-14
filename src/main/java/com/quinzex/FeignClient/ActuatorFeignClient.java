package com.quinzex.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "actuatorClient",url = "http://localhost:9090")
public interface ActuatorFeignClient {
    @GetMapping("/actuator/health")
    Map<String, Object> getHealth();


}
