package com.project.api.gestaoconsultas.controller;

import com.project.api.gestaoconsultas.dto.request.PacienteRequestDTO;
import com.project.api.gestaoconsultas.dto.response.PacienteResponseDTO;
import com.project.api.gestaoconsultas.services.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/listar")
    public ResponseEntity<List<PacienteResponseDTO>> buscarTodos(){
        return ResponseEntity.ok(pacienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> criar(@RequestBody @Valid PacienteRequestDTO paciente) {
        return ResponseEntity.status(201).body(pacienteService.criar(paciente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> editar(@PathVariable Integer id,
                                          @RequestBody @Valid PacienteRequestDTO dados) {
        return ResponseEntity.ok(pacienteService.editar(id, dados));
    }
}
