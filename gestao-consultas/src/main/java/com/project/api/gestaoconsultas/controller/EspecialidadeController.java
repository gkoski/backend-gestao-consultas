package com.project.api.gestaoconsultas.controller;

import com.project.api.gestaoconsultas.entities.mapper.Especialidade;
import com.project.api.gestaoconsultas.services.EspecialidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping("/listar")
    public ResponseEntity<List<Especialidade>> buscarTodos() {
        return ResponseEntity.ok(especialidadeService.listar());
    }

    @PostMapping
    public ResponseEntity<Especialidade> criar(@RequestBody @Valid Especialidade especialidade) {
        return ResponseEntity.status(201).body(especialidadeService.criar(especialidade));
    }
}
