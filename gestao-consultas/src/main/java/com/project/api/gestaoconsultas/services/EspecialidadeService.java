package com.project.api.gestaoconsultas.services;

import com.project.api.gestaoconsultas.entities.mapper.Especialidade;
import com.project.api.gestaoconsultas.repositories.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    public List<Especialidade> listar(){
        return  especialidadeRepository.findAll();
    }

    public Especialidade criar(Especialidade especialidade) {
        return especialidadeRepository.save(especialidade);
    }
}
