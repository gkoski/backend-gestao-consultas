package com.project.api.gestaoconsultas.controller;

import com.project.api.gestaoconsultas.dto.request.UsuarioRequestDTO;
import com.project.api.gestaoconsultas.dto.response.UsuarioResponseDTO;
import com.project.api.gestaoconsultas.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTodos(){
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(201).body(usuarioService.criar(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> editar(@PathVariable Integer id,
                                           @RequestBody @Valid UsuarioRequestDTO dados) {
        return ResponseEntity.ok(usuarioService.editar(id, dados));
    }

    @PutMapping("/{id}/resetar-senha")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resetarSenha(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        usuarioService.resetarSenha(id, body.get("senha"));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
