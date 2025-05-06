package br.com.plataformafly.usuarioapi.model.dto.in;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link br.com.plataformafly.usuarioapi.model.Usuario}
 */
@Data
public class UsuarioDTO implements Serializable {

    Integer id;
    String nome;
    String login;
    String password;
    String email;
    Boolean admin;
    LocalDateTime criadoEm;
    LocalDateTime atualizadoEm;
}