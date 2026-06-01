package com.project.api.gestaoconsultas.repositories;

import com.project.api.gestaoconsultas.entities.mapper.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Integer> {
}
