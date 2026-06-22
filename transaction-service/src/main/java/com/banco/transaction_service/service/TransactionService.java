package com.banco.transaction_service.service;

import com.banco.transaction_service.dto.OperacionRequest;
import com.banco.transaction_service.dto.TransaccionResponse;
import com.banco.transaction_service.dto.TransferenciaRequest;

public interface TransactionService {
    TransaccionResponse transferir(TransferenciaRequest request);
    TransaccionResponse depositar(OperacionRequest request);
    TransaccionResponse retirar(OperacionRequest request);
}
