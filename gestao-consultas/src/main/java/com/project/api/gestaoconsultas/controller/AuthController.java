package com.project.api.gestaoconsultas.controller;

import org.springframework.security.core.Authentication;
import com.project.api.gestaoconsultas.dto.request.LoginRequestDTO;
import com.project.api.gestaoconsultas.entities.mapper.Usuario;
import com.project.api.gestaoconsultas.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hash")
    public String gerarHash(@RequestParam String senha) {
        return passwordEncoder.encode(senha);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );

        Usuario usuario = (Usuario) auth.getPrincipal();

        String token = jwtService.gerarToken(usuario.getEmail(), usuario.getPerfil());
        return ResponseEntity.ok(token);
    }
}
