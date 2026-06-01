package com.project.api.gestaoconsultas.repositories;

import com.project.api.gestaoconsultas.entities.mapper.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {

    List<Consulta> findByPacienteId(Integer idPaciente);

    List<Consulta> findByDentistaId(Integer idDentista);

    List<Consulta> findByUsuarioId(Integer idUsuario);

    boolean existsByDentistaIdAndDataInicioLessThanAndDataFimGreaterThan(
            Integer idDentista,
            LocalDateTime dataFim,
            LocalDateTime dataInicio
    );
}
