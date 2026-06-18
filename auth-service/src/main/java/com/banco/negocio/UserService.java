package com.banco.negocio;

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
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya existe");
		}
		if (appUserRepository.existsByEmail(request.email())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya existe");
		}

        Rol rolUser = rolRepository.findById(request.rolId())
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));

        Usuario user = Usuario.builder()
                .id(null)
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .rol(rolUser)
                .build();

		return mapToResponse(appUserRepository.save(user));
	}


	private UserResponse mapToResponse(Usuario user) {
		return new UserResponse(
				user.getId(),
				user.getUsername(),
				user.getEmail()
		);
	}
}
