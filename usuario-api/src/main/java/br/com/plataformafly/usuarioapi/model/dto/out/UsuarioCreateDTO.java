package br.com.plataformafly.usuarioapi.model.dto.out;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioCreateDTO {

    @NotBlank
    private String nome;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotNull
    private Boolean admin;
}
