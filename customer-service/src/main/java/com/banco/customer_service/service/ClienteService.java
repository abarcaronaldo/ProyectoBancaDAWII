package com.banco.customer_service.service;

import com.banco.customer_service.dto.ClienteRequest;
import com.banco.customer_service.dto.ClienteResponse;

import java.util.List;

public interface ClienteService {
    ClienteResponse crearCliente(ClienteRequest request);
    ClienteResponse obtenerPorId(Long id);
    ClienteResponse obtenerPorDni(String dni);
    List<ClienteResponse> listarTodos();
    ClienteResponse actualizarCliente(Long id, ClienteRequest request);
    void desactivarCliente(Long id);
}
