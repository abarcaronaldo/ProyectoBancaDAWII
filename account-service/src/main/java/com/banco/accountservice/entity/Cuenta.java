package com.banco.accountservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", unique = true, nullable = false, length = 20)
    private String numeroCuenta;

    @Column(unique = true, nullable = false, length = 20)
    private String cci;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private String tipoCuenta;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private Boolean activo;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.activo == null) {
            this.activo = true;
        }
    }
}
