package br.com.plataformafly.authapi.model.dto;

import java.util.List;

public record UsuarioDTO(Long id, String nome, String login, String email, List<String> roles) {
}
