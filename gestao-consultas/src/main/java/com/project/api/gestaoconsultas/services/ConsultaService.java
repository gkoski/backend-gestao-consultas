package com.project.api.gestaoconsultas.services;

import com.project.api.gestaoconsultas.dto.request.ConsultaRequestDTO;
import com.project.api.gestaoconsultas.dto.response.ConsultaResponseDTO;
import com.project.api.gestaoconsultas.entities.mapper.Consulta;
import com.project.api.gestaoconsultas.entities.mapper.Dentista;
import com.project.api.gestaoconsultas.entities.mapper.Paciente;
import com.project.api.gestaoconsultas.entities.mapper.Usuario;
import com.project.api.gestaoconsultas.repositories.ConsultaRepository;
import com.project.api.gestaoconsultas.repositories.DentistaRepository;
import com.project.api.gestaoconsultas.repositories.PacienteRepository;
import com.project.api.gestaoconsultas.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private DentistaRepository dentistaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ConsultaResponseDTO> listar(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        List<Consulta> consultas;

        if (usuario.getPerfil().equals("ADMIN")) {
            consultas = consultaRepository.findAll();
        } else {
            consultas = consultaRepository.findByUsuarioId(usuario.getId());
        }

        return consultas.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public ConsultaResponseDTO buscarPorId(Integer id) {
        Consulta consulta = buscarPorIdEntidade(id);
        return converterParaDTO(consulta);
    }

    public ConsultaResponseDTO criar(ConsultaRequestDTO dto) {
        if (dto.getDataInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A data de início deve ser no futuro");
        }

        if (dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new RuntimeException("A data fim deve ser depois da data início");
        }

        boolean conflito = consultaRepository
                .existsByDentistaIdAndDataInicioLessThanAndDataFimGreaterThan(
                        dto.getIdDentista(),
                        dto.getDataFim(),
                        dto.getDataInicio()
                );

        if (conflito) {
            throw new RuntimeException("Dentista já possui uma consulta nesse horário");
        }

        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        Dentista dentista = dentistaRepository.findById(dto.getIdDentista())
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setDentista(dentista);
        consulta.setUsuario(usuario);
        consulta.setDescricao(dto.getDescricao());
        consulta.setDataInicio(dto.getDataInicio());
        consulta.setDataFim(dto.getDataFim());
        consulta.setStatus("AGENDADA");

        Consulta salvo = consultaRepository.save(consulta);
        return converterParaDTO(salvo);
    }

    public ConsultaResponseDTO cancelar(Integer id, String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new RuntimeException("Motivo é obrigatório para cancelar uma consulta");
        }

        Consulta consulta = buscarPorIdEntidade(id);
        consulta.setMotivoCancelamento(motivo);
        consulta.setStatus("CANCELADA");

        Consulta salvo = consultaRepository.save(consulta);
        return converterParaDTO(salvo);
    }

    public ConsultaResponseDTO finalizar(Integer id) {
        Consulta consulta = buscarPorIdEntidade(id);
        consulta.setStatus("FINALIZADA");

        Consulta salvo = consultaRepository.save(consulta);
        return converterParaDTO(salvo);
    }

    private Consulta buscarPorIdEntidade(Integer id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }

    private ConsultaResponseDTO converterParaDTO(Consulta consulta) {
        ConsultaResponseDTO response = new ConsultaResponseDTO();
        response.setId(consulta.getId());
        response.setNomePaciente(consulta.getPaciente().getNome());
        response.setNomeDentista(consulta.getDentista().getNome());
        response.setNomeUsuario(consulta.getUsuario().getNome());
        response.setDescricao(consulta.getDescricao());
        response.setDataInicio(consulta.getDataInicio());
        response.setDataFim(consulta.getDataFim());
        response.setDataRegistro(consulta.getDataRegistro());
        response.setMotivoCancelamento(consulta.getMotivoCancelamento());
        response.setStatus(consulta.getStatus());
        return response;
    }
}
