package br.com.plataformafly.authapi.model.dto;

//public record UsuarioDTO(Long id, String nome, String login, String email, List<String> roles) {
//}

public record UsuarioDTO(
        Long id,
        String nome,
        String login,
        String email,
        String password,
        Boolean admin
) {
}
