package br.com.plataformafly.authapi.service.impl;

import br.com.plataformafly.authapi.external.UsuarioClient;
import br.com.plataformafly.authapi.model.dto.UsuarioDTO;
import br.com.plataformafly.authapi.model.dto.in.LoginRequestDTO;
import br.com.plataformafly.authapi.model.dto.out.LoginResponseDTO;
import br.com.plataformafly.authapi.security.jwt.JwtTokenProvider;
import br.com.plataformafly.authapi.security.service.CustomUserDetailsService;
import br.com.plataformafly.authapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioClient usuarioClient;

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
        UsuarioDTO usuario = usuarioClient.buscarPorLogin(dto.login());

        if (!passwordEncoder.matches(dto.password(), usuario.password())) {
            throw new RuntimeException("Senha inválida");
        }

        var auth = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        authenticationManager.authenticate(auth);

        List<String> roles = List.of(usuario.admin() ? "ROLE_ADMIN" : "ROLE_USER");

        var userDetails = userDetailsService.loadUserByUsername(dto.login());
        String token = tokenProvider.generateToken(userDetails, roles);

        return new LoginResponseDTO(token, roles);
    }
}
