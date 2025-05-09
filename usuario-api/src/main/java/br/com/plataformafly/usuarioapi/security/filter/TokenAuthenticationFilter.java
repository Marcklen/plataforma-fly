package br.com.plataformafly.usuarioapi.security.filter;

import br.com.plataformafly.usuarioapi.security.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain) throws ServletException, IOException {
        String token = tokenService.getTokenFromHeader(request);

        if (token != null && tokenService.isTokenValid(token)) {
            String username = tokenService.getTokenUsername(token);
            List<String> roles = tokenService.getTokenRoles(token);
            var authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

            var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            // IMPORTANTE: garante que requisição anônima não herde contexto anterior
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}