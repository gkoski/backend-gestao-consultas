package com.project.api.gestaoconsultas.repositories;

import com.project.api.gestaoconsultas.entities.mapper.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    Optional<Paciente> findByCpf(String cpf);

    Optional<Paciente> findByEmail(String email);
}
