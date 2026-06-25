package com.banco.accountservice.controller;

import com.banco.accountservice.dto.CuentaCreateRequest;
import com.banco.accountservice.dto.CuentaResponse;
import com.banco.accountservice.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaResponse> crearCuenta(@RequestBody CuentaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.crearCuenta(request));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> listarCuentas() {
        return ResponseEntity.ok(cuentaService.listarCuentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerPorId(id));
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<CuentaResponse>> obtenerPorClienteId(Principal principal) {
        return ResponseEntity.ok(cuentaService.obtenerCuentaUsuarioLogueado(principal));
    }

    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<CuentaResponse> obtenerPorNumero(@PathVariable String numeroCuenta) {
        return ResponseEntity.ok(cuentaService.obtenerPorNumeroCuenta(numeroCuenta));
    }

    //desactivar una cuenta (admin)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CuentaResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo) {
        return ResponseEntity.ok(cuentaService.cambiarEstadoCuenta(id, activo));
    }

    //actualizar saldo (admin)
    @PutMapping("/{id}/saldo")
    public ResponseEntity<CuentaResponse> actualizarSaldo(
            @PathVariable Long id,
            @RequestParam java.math.BigDecimal monto) {
        return ResponseEntity.ok(cuentaService.actualizarSaldo(id, monto));
    }
}
