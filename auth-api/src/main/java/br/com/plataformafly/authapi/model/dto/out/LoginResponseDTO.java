package br.com.plataformafly.authapi.model.dto.out;

import java.util.List;

public record LoginResponseDTO(String token, List<String> roles) {
}
