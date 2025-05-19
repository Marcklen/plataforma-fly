package br.com.plataformafly.authapi.external;

import br.com.plataformafly.authapi.controller.exceptions.handler.CredenciaisInvalidasException;
import br.com.plataformafly.authapi.model.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class UsuarioClient {

    private final RestTemplate restTemplate;

    @Value("${usuario.api.url}")
    private String usuarioApiBaseUrl;

    public UsuarioDTO buscarPorLogin(String login) {
        String url = UriComponentsBuilder
                .fromHttpUrl(usuarioApiBaseUrl + "/login/{login}")
                .buildAndExpand(login)
                .toUriString();
        try {
            return restTemplate.getForObject(url, UsuarioDTO.class);
        } catch (HttpClientErrorException e) {
            throw new CredenciaisInvalidasException("Usuário não encontrado!");
        }

    }
}