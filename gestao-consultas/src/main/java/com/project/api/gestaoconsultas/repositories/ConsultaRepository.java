package com.project.api.gestaoconsultas.repositories;

import com.project.api.gestaoconsultas.entities.mapper.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT c FROM Consulta c WHERE " +
    "(:status IS NULL OR c.status = :status) AND " +
    "(:idDentista IS NULL OR c.dentista.id = :idDentista) AND " +
    "(:idPaciente IS NULL OR c.paciente.id = :idPaciente) AND " +
    "(:idUsuario IS NULL OR c.usuario.id = :idUsuario) AND " +
    "(:dataInicio IS NULL OR c.dataInicio >= :dataInicio) AND " +
    "(:dataFim IS NULL OR c.dataFim <= :dataFim)")

    List<Consulta> buscarComFiltros(
            @Param("status") String status,
            @Param("idDentista") Integer idDentista,
            @Param("idPaciente") Integer idPaciente,
            @Param("idUsuario") Integer idUsuario,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}
