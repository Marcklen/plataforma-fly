package br.com.plataformafly.authapi.service.impl;

import br.com.plataformafly.authapi.model.dto.in.LoginRequestDTO;
import br.com.plataformafly.authapi.model.dto.out.LoginResponseDTO;
import br.com.plataformafly.authapi.security.jwt.JwtTokenProvider;
import br.com.plataformafly.authapi.security.service.CustomUserDetailsService;
import br.com.plataformafly.authapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
        var auth = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        authenticationManager.authenticate(auth);

        var userDetails = userDetailsService.loadUserByUsername(dto.login());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        String token = tokenProvider.generateToken(userDetails, roles);
        return new LoginResponseDTO(token, roles);
    }
}
