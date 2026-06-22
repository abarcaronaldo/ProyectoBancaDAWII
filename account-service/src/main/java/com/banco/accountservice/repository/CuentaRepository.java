package com.banco.accountservice.repository;

import com.banco.accountservice.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    //para buscar la cuenta especifica de un cliente
    List<Cuenta> findByClienteId(Long clienteId);

    //para buscar por el número de cuenta
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    //para validar si un numero generado al azar ya existe en la BD
    boolean existsByNumeroCuenta(String numeroCuenta);
    boolean existsByCci(String cci);
}