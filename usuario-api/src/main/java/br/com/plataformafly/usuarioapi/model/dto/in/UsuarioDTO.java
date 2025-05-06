package br.com.plataformafly.usuarioapi.model.dto.in;

import br.com.plataformafly.usuarioapi.model.dto.out.RoleDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UsuarioDTO implements Serializable {

    private Integer id;
    private String nome;
    private String login;
    private String password;
    private String email;
    private Set<RoleDto> roles;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}