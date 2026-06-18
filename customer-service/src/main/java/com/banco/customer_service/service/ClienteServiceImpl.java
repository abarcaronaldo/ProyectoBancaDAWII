package com.banco.customer_service.service;

import com.banco.customer_service.client.AuthClient;
import com.banco.customer_service.dto.ClienteRequest;
import com.banco.customer_service.dto.ClienteResponse;
import com.banco.customer_service.dto.UserRequest;
import com.banco.customer_service.entity.Cliente;
import com.banco.customer_service.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;
    private final AuthClient authClient;

    @Transactional
    @Override
    public ClienteResponse crearCliente(ClienteRequest request) {

        if (clienteRepository.existsByDni(request.dni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado en el sistema");
        }
        if (clienteRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo electrónico ya está registrado");
        }

        Cliente cliente = Cliente.builder()
                .dni(request.dni())
                .nombre(request.nombre())
                .apellido(request.apellido())
                .email(request.email())
                .telefono(request.telefono())
                .direccion(request.direccion())
                .build();
        Cliente clienteGuardado = clienteRepository.save(cliente);

        try {
            UserRequest userRequest = new UserRequest(
                    clienteGuardado.getDni(),
                    clienteGuardado.getEmail(),
                    clienteGuardado.getDni(),
                    2L
            );

            authClient.registrarUsuarioBanco(userRequest);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Cliente no registrado. Falló la comunicación con el servicio: " + e.getMessage());
        }

        return mapToResponse(clienteGuardado);
    }

    @Override
    public ClienteResponse obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con el ID: " + id));
        return mapToResponse(cliente);
    }

    @Override
    public ClienteResponse obtenerPorDni(String dni) {
        Cliente cliente = clienteRepository.findByDni(dni)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con el DNI: " + dni));
        return mapToResponse(cliente);
    }

    @Override
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponse actualizarCliente(Long id, ClienteRequest request) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        if (!clienteExistente.getDni().equals(request.dni()) && clienteRepository.existsByDni(request.dni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nuevo DNI ya está en uso");
        }
        if (!clienteExistente.getEmail().equals(request.email()) && clienteRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nuevo correo ya está en uso");
        }

        clienteExistente.setDni(request.dni());
        clienteExistente.setNombre(request.nombre());
        clienteExistente.setApellido(request.apellido());
        clienteExistente.setEmail(request.email());
        clienteExistente.setTelefono(request.telefono());
        clienteExistente.setDireccion(request.direccion());
        return mapToResponse(clienteRepository.save(clienteExistente));
    }

    @Override
    public void desactivarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    private ClienteResponse mapToResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getDni(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getCreadoEn(),
                cliente.getActivo()
        );
    }
}
