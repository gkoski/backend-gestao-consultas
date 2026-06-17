package com.project.api.gestaoconsultas.services;

import com.project.api.gestaoconsultas.dto.request.UsuarioRequestDTO;
import com.project.api.gestaoconsultas.dto.response.UsuarioResponseDTO;
import com.project.api.gestaoconsultas.entities.mapper.Usuario;
import com.project.api.gestaoconsultas.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDTO> response = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setId(usuario.getId());
            dto.setNome(usuario.getNome());
            dto.setCpf(usuario.getCpf());
            dto.setEmail(usuario.getEmail());
            dto.setPerfil(usuario.getPerfil());
            dto.setAtivo(usuario.getAtivo());
            response.add(dto);
        }
        return response;
    }

    public UsuarioResponseDTO buscarPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setCpf(usuario.getCpf());
        response.setEmail(usuario.getEmail());
        response.setPerfil(usuario.getPerfil());
        response.setAtivo(usuario.getAtivo());

        return response;
    }

    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setPerfil(dto.getPerfil());
        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(salvo.getId());
        response.setNome(salvo.getNome());
        response.setEmail(salvo.getEmail());
        response.setCpf(salvo.getCpf());
        response.setPerfil(salvo.getPerfil());
        response.setAtivo(salvo.getAtivo());

        return response;
    }

    public UsuarioResponseDTO editar(Integer id, UsuarioRequestDTO dados) {
        Usuario usuario = buscarPorIdEntidade(id);
        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setCpf(dados.getCpf());
        usuario.setPerfil(dados.getPerfil());

        Usuario salvo = usuarioRepository.save(usuario);

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(salvo.getId());
        response.setNome(salvo.getNome());
        response.setCpf(salvo.getCpf());
        response.setEmail(salvo.getEmail());
        response.setPerfil(salvo.getPerfil());
        response.setAtivo(salvo.getAtivo());

        return response;
    }

    public void resetarSenha(Integer id, String novaSenha) {
        Usuario usuario = buscarPorIdEntidade(id);
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    public void deletar(Integer id) {
        buscarPorId(id); // garante que o usuário existe antes de deletar
        usuarioRepository.deleteById(id);
    }

    private Usuario buscarPorIdEntidade(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
