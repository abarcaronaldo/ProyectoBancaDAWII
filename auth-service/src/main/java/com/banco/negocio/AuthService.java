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
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;

	public AuthService(
			JwtService jwtService,
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager
	) {
		this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
	}

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            Usuario user = usuarioRepository.findByUsername(request.username())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

            if (!user.isActivo()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Su cuenta aún no ha sido activada. Por favor, establezca su contraseña primero.");
            }

            String token = jwtService.generateToken(user);
            return new LoginResponse(token, "Bearer", jwtService.getExpirationMinutes());

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }
    }
}
