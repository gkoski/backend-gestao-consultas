package com.project.api.gestaoconsultas.controller;

import com.project.api.gestaoconsultas.dto.request.DentistaRequestDTO;
import com.project.api.gestaoconsultas.dto.response.DentistaResponseDTO;
import com.project.api.gestaoconsultas.services.DentistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dentistas")
public class DentistaController {

@Autowired
private DentistaService dentistaService;

@GetMapping("/listar")
public ResponseEntity<List<DentistaResponseDTO>> buscarTodos() {
    return ResponseEntity.ok(dentistaService.listar());
}

@GetMapping("/{id}")
public ResponseEntity<DentistaResponseDTO> buscarPorId(@PathVariable Integer id) {
    return ResponseEntity.ok(dentistaService.buscarPorId(id));
}

@PostMapping
public ResponseEntity<DentistaResponseDTO> criar(@RequestBody @Valid DentistaRequestDTO dentista) {
    return ResponseEntity.status(201).body(dentistaService.criar(dentista));
}

@PutMapping("/{id}")
public ResponseEntity<DentistaResponseDTO> editar(@PathVariable Integer id,
                                       @RequestBody @Valid DentistaRequestDTO dados) {
    return ResponseEntity.ok(dentistaService.editar(id, dados));
}

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<DentistaResponseDTO> desativar(@PathVariable Integer id) {
        return ResponseEntity.ok(dentistaService.desativar(id));
    }
}
