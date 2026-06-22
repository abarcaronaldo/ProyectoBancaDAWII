package com.banco.transaction_service.client;

import com.banco.transaction_service.dto.CuentaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "account-service", configuration = FeignClientConfig.class)
public interface AccountClient {
    @GetMapping("/api/cuentas/numero/{numeroCuenta}")
    CuentaDto obtenerPorNumeroCuenta(@PathVariable("numeroCuenta") String numeroCuenta);

    @PutMapping("/api/cuentas/{id}/saldo")
    CuentaDto actualizarSaldo(
            @PathVariable("id") Long id,
            @RequestParam("monto") BigDecimal monto
    );
}
