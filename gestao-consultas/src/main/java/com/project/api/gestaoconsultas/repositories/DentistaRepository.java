package com.project.api.gestaoconsultas.repositories;

import com.project.api.gestaoconsultas.entities.mapper.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Integer> {

    Optional<Dentista> findByCpf(String cpf);

    Optional<Dentista> findByEmail(String email);
}
