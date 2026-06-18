package com.banco.customer_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 8)
    private String dni;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Email(message = "El formato del correo no es válido")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(length = 9)
    private String telefono;

    @Column(length = 150)
    private String direccion;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "estado", nullable = false)
    private Boolean activo;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
        this.activo = true;
    }
}
