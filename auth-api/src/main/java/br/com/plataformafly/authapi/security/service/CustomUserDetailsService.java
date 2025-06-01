package br.com.plataformafly.authapi.security.service;

import br.com.plataformafly.authapi.controller.exceptions.handler.UsuarioNaoEncontradoException;
import br.com.plataformafly.authapi.model.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Value("${usuario.api.url}")
    private String usuarioApiUrl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsuarioNaoEncontradoException {
        try {
            String url = usuarioApiUrl + "/login/" + username;
            UsuarioDTO usuario = restTemplate.getForObject(url, UsuarioDTO.class);

            if (usuario == null || usuario.login() == null || !usuario.login().equals(username)) {
                throw new UsuarioNaoEncontradoException("Usuário não encontrado");
            }

            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority(usuario.admin() ? "ROLE_ADMIN" : "ROLE_USER")
            );

            return new User(usuario.login(), usuario.password(), authorities);
        } catch (HttpClientErrorException.NotFound e) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
        }
    }
}