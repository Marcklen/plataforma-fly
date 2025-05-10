package br.com.plataformafly.usuarioapi.model.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDTO {

    private String remetente;
    private String destinatario;
    @NotBlank(message = "O assunto não pode estar em branco")
    private String assunto;
    @NotBlank(message = "O corpo não pode estar em branco")
    private String corpo;
}
