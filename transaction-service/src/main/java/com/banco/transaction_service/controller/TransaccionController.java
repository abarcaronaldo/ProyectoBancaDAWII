package com.banco.transaction_service.controller;

import com.banco.transaction_service.dto.OperacionRequest;
import com.banco.transaction_service.dto.TransaccionResponse;
import com.banco.transaction_service.dto.TransferenciaRequest;
import com.banco.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {
    private final TransactionService transactionService;

    @PostMapping("/transferencia")
    public ResponseEntity<TransaccionResponse> transferir(@RequestBody TransferenciaRequest request) {
        return ResponseEntity.ok(transactionService.transferir(request));
    }

    @PostMapping("/deposito")
    public ResponseEntity<TransaccionResponse> depositar(@RequestBody OperacionRequest request) {
        return ResponseEntity.ok(transactionService.depositar(request));
    }

    @PostMapping("/retiro")
    public ResponseEntity<TransaccionResponse> retirar(@RequestBody OperacionRequest request) {
        return ResponseEntity.ok(transactionService.retirar(request));
    }
}
