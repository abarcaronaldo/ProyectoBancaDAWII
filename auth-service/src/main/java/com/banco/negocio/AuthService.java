package com.banco.negocio;

import com.banco.dto.LoginRequest;
import com.banco.dto.LoginResponse;
import com.banco.entidades.Usuario;
import com.banco.repositorio.UsuarioRepository;
import com.banco.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

	private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

	public AuthService(
			JwtService jwtService,
            AuthenticationManager authenticationManager
	) {
		this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
	}

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            Usuario user = (Usuario) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            return new LoginResponse(token, "Bearer", jwtService.getExpirationMinutes());

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }
    }
}
