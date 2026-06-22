package com.banco.customer_service.controller;

import com.banco.customer_service.dto.ClienteRequest;
import com.banco.customer_service.dto.ClienteResponse;
import com.banco.customer_service.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse nuevoCliente = clienteService.crearCliente(request);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @GetMapping("/dni/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponse> obtenerPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(clienteService.obtenerPorDni(dni));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.actualizarCliente(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        clienteService.desactivarCliente(id);
        return ResponseEntity.noContent().build();
    }

    //CLIENTE
    @GetMapping("/mi-perfil")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponse> verMiPanel(Principal principal) {
        String dniCliente = principal.getName();
        return ResponseEntity.ok(clienteService.obtenerPorDni(dniCliente));
    }
}
