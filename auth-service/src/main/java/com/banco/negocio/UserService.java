package com.banco.negocio;

import com.banco.dto.ActivarCuentaRequest;
import com.banco.dto.UserRequest;
import com.banco.dto.UserResponse;
import com.banco.entidades.Rol;
import com.banco.entidades.Usuario;
import com.banco.repositorio.RolRepository;
import com.banco.repositorio.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

	private final UsuarioRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

	public UserService(UsuarioRepository appUserRepository,
                       PasswordEncoder passwordEncoder,
                       RolRepository rolRepository) {
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
	}

    public UserResponse createUser(UserRequest request) {
        if (appUserRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username (DNI) ya existe");
        }
        if (appUserRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya existe");
        }

        Rol rolUser = rolRepository.findById(request.rolId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error: Rol no encontrado."));

        String tokenUnico = java.util.UUID.randomUUID().toString();

        Usuario user = Usuario.builder()
                .id(null)
                .username(request.username())
                .email(request.email())
                .password(null)
                .rol(rolUser)
                .activo(false)
                .tokenActivacion(tokenUnico)
                .build();

        Usuario usuarioGuardado = appUserRepository.save(user);
        return mapToResponse(usuarioGuardado);
    }

    public void activarCuenta(ActivarCuentaRequest request) {

        Usuario user = appUserRepository.findByTokenActivacion(request.token())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token de activación inválido o expirado."));

        user.setPassword(passwordEncoder.encode(request.nuevaPassword()));
        user.setActivo(true);
        user.setTokenActivacion(null);
        appUserRepository.save(user);
    }


    public List<UserResponse> listarTodosLosUsuarios() {
        return appUserRepository.findAll().stream()
                .map(usuario -> new UserResponse(
                        usuario.getId(),
                        usuario.getUsername(),
                        usuario.getEmail(),
                        usuario.isActivo(),
                        usuario.getTokenActivacion(),
                        usuario.getRol().getNombre()
                ))
                .collect(Collectors.toList());
    }

    private UserResponse mapToResponse(Usuario user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isActivo(),
                user.getTokenActivacion(),
                user.getRol().getNombre()
        );
    }
}
