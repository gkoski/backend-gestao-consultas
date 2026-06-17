package com.project.api.gestaoconsultas.controller;

import com.project.api.gestaoconsultas.dto.request.ConsultaRequestDTO;
import com.project.api.gestaoconsultas.dto.response.ConsultaResponseDTO;
import com.project.api.gestaoconsultas.entities.mapper.Usuario;
import com.project.api.gestaoconsultas.services.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/listar")
    public ResponseEntity<List<ConsultaResponseDTO>> buscarTodos(Authentication authentication) {
        return ResponseEntity.ok(consultaService.listar(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @PostMapping("/criar")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA')")
    public ResponseEntity<ConsultaResponseDTO> criar(
            @RequestBody @Valid ConsultaRequestDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        dto.setIdUsuario(usuarioLogado.getId());   // descomenta isso
        return ResponseEntity.status(201).body(consultaService.criar(dto));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ConsultaResponseDTO> cancelar(@PathVariable Integer id,
                                                        @RequestParam String motivo) {
        return ResponseEntity.ok(consultaService.cancelar(id, motivo));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<ConsultaResponseDTO> finalizar(@PathVariable Integer id) {
        return ResponseEntity.ok(consultaService.finalizar(id));
    }

    @GetMapping("/relatorios")
    public ResponseEntity<List<ConsultaResponseDTO>> buscarComFiltros(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer idDentista,
            @RequestParam(required = false) Integer idPaciente,
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) LocalDateTime dataInicio,
            @RequestParam(required = false) LocalDateTime dataFim) {

        return ResponseEntity.ok(consultaService.buscarComFiltros(
                status, idDentista, idPaciente, idUsuario, dataInicio, dataFim
        ));
    }
}
