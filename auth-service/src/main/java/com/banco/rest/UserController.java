package com.banco.rest;

import com.banco.dto.ActivarCuentaRequest;
import com.banco.dto.UserRequest;
import com.banco.dto.UserResponse;
import com.banco.negocio.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping()
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
	}

    @PostMapping("/activar-cuenta")
    public ResponseEntity<Map<String, String>> activarCuenta(@RequestBody ActivarCuentaRequest request) {
        userService.activarCuenta(request);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Contraseña establecida con éxito. Tu cuenta ahora está ACTIVA.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> listarUsuarios() {
        return ResponseEntity.ok(userService.listarTodosLosUsuarios());
    }
}
