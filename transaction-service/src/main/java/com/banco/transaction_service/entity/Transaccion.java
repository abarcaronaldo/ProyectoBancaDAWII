package com.banco.transaction_service.entity;

import com.banco.transaction_service.enums.EstadoTransaccion;
import com.banco.transaction_service.enums.TipoTransaccion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cuenta_origen")
    private String cuentaOrigen;

    @Column(name = "cuenta_destino")
    private String cuentaDestino;

    @Column(nullable = false)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransaccion tipo; // TRANSFERENCIA, DEPOSITO, RETIRO

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTransaccion estado; // COMPLETADA, RECHAZADA, FALLIDA

    @Column(name = "fecha_transaccion")
    private LocalDateTime fechaTransaccion;

    @PrePersist
    protected void onCreate() {
        this.fechaTransaccion = LocalDateTime.now();
    }
}
