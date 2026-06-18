package com.banco;

import com.banco.entidades.Rol;
import com.banco.entidades.Usuario;
import com.banco.repositorio.RolRepository;
import com.banco.repositorio.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

    @Bean
    CommandLineRunner initData(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (rolRepository.count() == 0) {
                Rol adminRol = new Rol();
                adminRol.setId(1L);
                adminRol.setNombre("ROLE_ADMIN");
                rolRepository.save(adminRol);

                Rol clienteRol = new Rol();
                clienteRol.setId(2L);
                clienteRol.setNombre("ROLE_CLIENTE");
                rolRepository.save(clienteRol);

                System.out.println("Roles base (ADMIN y CLIENTE) creados");
            }


            if (usuarioRepository.count() == 0) {
                Rol adminRol = rolRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));

                Usuario adminUser = new Usuario();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@banco.com");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setRol(adminRol);

                usuarioRepository.save(adminUser);

                System.out.println("Usuario Administrador inicial creado");
                System.out.println("-> Username: admin");
                System.out.println("-> Password: admin123");
            }
        };
    }
}
