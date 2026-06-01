package com.project.api.gestaoconsultas.services;

import com.project.api.gestaoconsultas.dto.request.PacienteRequestDTO;
import com.project.api.gestaoconsultas.dto.response.PacienteResponseDTO;
import com.project.api.gestaoconsultas.entities.mapper.Paciente;
import com.project.api.gestaoconsultas.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<PacienteResponseDTO> listar() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        List<PacienteResponseDTO> response = new ArrayList<>();

        for (Paciente paciente : pacientes) {
            PacienteResponseDTO dto = new PacienteResponseDTO();
            dto.setId(paciente.getId());
            dto.setNome(paciente.getNome());
            dto.setCpf(paciente.getCpf());
            dto.setEmail(paciente.getEmail());
            dto.setTelefone(paciente.getTelefone());
            response.add(dto);
        }
        return response;
    }

    public PacienteResponseDTO buscarPorId(Integer id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        PacienteResponseDTO response = new PacienteResponseDTO();
        response.setId(paciente.getId());
        response.setNome(paciente.getNome());
        response.setCpf(paciente.getCpf());
        response.setEmail(paciente.getEmail());
        response.setTelefone(paciente.getTelefone());

        return response;
    }

    public PacienteResponseDTO criar(PacienteRequestDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNome(dto.getNome());
        paciente.setEmail(dto.getEmail());
        paciente.setCpf(dto.getCpf());
        paciente.setTelefone(dto.getTelefone());

        Paciente salvo = pacienteRepository.save(paciente);

        PacienteResponseDTO response = new PacienteResponseDTO();
        response.setId(salvo.getId());
        response.setNome(salvo.getNome());
        response.setEmail(salvo.getEmail());
        response.setCpf(salvo.getCpf());
        response.setTelefone(salvo.getTelefone());

        return response;
    }

    public PacienteResponseDTO editar(Integer id, PacienteRequestDTO dados) {
        Paciente paciente = buscarPorIdEntidade(id);
        paciente.setNome(dados.getNome());
        paciente.setEmail(dados.getEmail());
        paciente.setCpf(dados.getCpf());
        paciente.setTelefone(dados.getTelefone());

        Paciente salvo = pacienteRepository.save(paciente);

        PacienteResponseDTO response = new PacienteResponseDTO();
        response.setId(salvo.getId());
        response.setNome(salvo.getNome());
        response.setCpf(salvo.getCpf());
        response.setEmail(salvo.getEmail());
        response.setTelefone(salvo.getTelefone());

        return response;
    }

    private Paciente buscarPorIdEntidade(Integer id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }
}
