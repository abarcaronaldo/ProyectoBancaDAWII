package com.banco.customer_service.client;

import com.banco.customer_service.dto.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", configuration = FeignClientConfig.class)
public interface AuthClient {
    @PostMapping("/api/users")
    void registrarUsuarioBanco(@RequestBody UserRequest userRequest);
}
