package com.project.api.gestaoconsultas.services;

import com.project.api.gestaoconsultas.dto.request.DentistaRequestDTO;
import com.project.api.gestaoconsultas.dto.response.DentistaResponseDTO;
import com.project.api.gestaoconsultas.entities.mapper.Dentista;
import com.project.api.gestaoconsultas.repositories.DentistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DentistaService {

    @Autowired
    private DentistaRepository dentistaRepository;

    public List<DentistaResponseDTO> listar() {
        List<Dentista> dentistas = dentistaRepository.findAll();
        List<DentistaResponseDTO> response = new ArrayList<>();

        for (Dentista dentista : dentistas) {
            DentistaResponseDTO dto = new DentistaResponseDTO();
            dto.setId(dentista.getId());
            dto.setNome(dentista.getNome());
            dto.setCpf(dentista.getCpf());
            dto.setEmail(dentista.getEmail());
            dto.setCro(dentista.getCro());
            dto.setAtivo(dentista.getAtivo());
            response.add(dto);
        }
        return response;
    }

    public DentistaResponseDTO buscarPorId(Integer id) {
        Dentista dentista = dentistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));

        DentistaResponseDTO response = new DentistaResponseDTO();
        response.setId(dentista.getId());
        response.setNome(dentista.getNome());
        response.setCpf(dentista.getCpf());
        response.setEmail(dentista.getEmail());
        response.setCro(dentista.getCro());
        response.setAtivo(dentista.getAtivo());

        return response;
    }

    public DentistaResponseDTO criar(DentistaRequestDTO dto) {
        Dentista dentista = new Dentista();
        dentista.setNome(dto.getNome());
        dentista.setEmail(dto.getEmail());
        dentista.setCpf(dto.getCpf());
        dentista.setCro(dto.getCro());
        dentista.setAtivo(true);

        Dentista salvo = dentistaRepository.save(dentista);

        DentistaResponseDTO response = new DentistaResponseDTO();
        response.setId(salvo.getId());
        response.setNome(salvo.getNome());
        response.setEmail(salvo.getEmail());
        response.setCpf(salvo.getCpf());
        response.setCro(salvo.getCro());
        response.setAtivo(salvo.getAtivo());

        return response;
    }

    public DentistaResponseDTO editar(Integer id, DentistaRequestDTO dados) {
        Dentista dentista = buscarPorIdEntidade(id);
        dentista.setNome(dados.getNome());
        dentista.setEmail(dados.getEmail());
        dentista.setCpf(dados.getCpf());
        dentista.setCro(dados.getCro());

        Dentista salvo = dentistaRepository.save(dentista);

        DentistaResponseDTO response = new DentistaResponseDTO();
        response.setId(salvo.getId());
        response.setNome(salvo.getNome());
        response.setCpf(salvo.getCpf());
        response.setEmail(salvo.getEmail());
        response.setCro(salvo.getCro());
        response.setAtivo(salvo.getAtivo());

        return response;
    }

    public DentistaResponseDTO desativar(Integer id) {
        Dentista dentista = buscarPorIdEntidade(id);
        dentista.setAtivo(false);
        Dentista salvo = dentistaRepository.save(dentista);

        DentistaResponseDTO response = new DentistaResponseDTO();
        response.setId(salvo.getId());
        response.setNome(salvo.getNome());
        response.setCpf(salvo.getCpf());
        response.setEmail(salvo.getEmail());
        response.setCro(salvo.getCro());
        response.setAtivo(salvo.getAtivo());

        return response;
    }

    private Dentista buscarPorIdEntidade(Integer id) {
        return dentistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
    }
}
