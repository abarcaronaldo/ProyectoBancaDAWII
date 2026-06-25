package com.banco.accountservice.client;

import com.banco.accountservice.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", configuration = FeignClientConfig.class)
public interface CustomerClient {
    @GetMapping("/api/clientes/{id}")
    ClienteDTO obtenerClientePorId(@PathVariable("id") Long id);

    @GetMapping("/api/clientes/dni/{dni}")
    ClienteDTO obtenerClientePorDni(@PathVariable("dni") String dni);
}
