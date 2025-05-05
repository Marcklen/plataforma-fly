package br.com.plataformafly.authapi.security.service;

import br.com.plataformafly.authapi.model.Usuario;
import br.com.plataformafly.authapi.model.repository.UsuarioRepository;
import br.com.plataformafly.authapi.security.dto.UserDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        return UserDetailsDTO.build(usuario);

//        return org.springframework.security.core.userdetails.User
//                .withUsername(usuario.getLogin())
//                .password(usuario.getPassword())
//                .roles(usuario.getAdmin() ? "ADMIN" : "USER")
//                .build();
    }
}
