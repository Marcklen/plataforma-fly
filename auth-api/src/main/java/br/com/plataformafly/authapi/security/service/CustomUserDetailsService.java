package br.com.plataformafly.authapi.security.service;

import br.com.plataformafly.authapi.model.dto.UsuarioDTO;
import br.com.plataformafly.authapi.security.dto.UserDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String baseUrl = "http://localhost:8081/usuario/login/" + username;
        try {
            ResponseEntity<UsuarioDTO> response = restTemplate.getForEntity(baseUrl, UsuarioDTO.class);
            UsuarioDTO dto = response.getBody();

            if (dto == null) {
                throw new UsernameNotFoundException("Usuário não encontrado");
            }

            return new UserDetailsDTO(
                    dto.login(),
                    dto.password(),
                    List.of(dto.admin() ? new SimpleGrantedAuthority("ROLE_ADMIN") : new SimpleGrantedAuthority("ROLE_USER"))

            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
        }
    }
}