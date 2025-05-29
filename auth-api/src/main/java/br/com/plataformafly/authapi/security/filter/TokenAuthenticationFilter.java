package br.com.plataformafly.authapi.security.filter;

import br.com.plataformafly.authapi.security.jwt.JwtTokenProvider;
import br.com.plataformafly.authapi.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    private static final List<String> AUTH_WHITELIST = List.of(
            "/auth/login",
            "/usuario",
            "/actuator/health",
            "/actuator/**"
    );


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        // Ignorar requisições da lista de exceções
        if (AUTH_WHITELIST.stream().anyMatch(path::equalsIgnoreCase)) {
            filterChain.doFilter(request, response);
            return;
        }
        // Obtém o token do cabeçalho Authorization
        String token = tokenProvider.getTokenFromRequest(request);
        // Verifica se o token é válido
        if (token != null && tokenProvider.isTokenValid(token)) {
            String username = tokenProvider.getTokenUsername(token);
            var userDetails = userDetailsService.loadUserByUsername(username);
            // Cria o objeto de autenticação
            var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            // Define o objeto de autenticação no contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // Continua a execução do filtro
        filterChain.doFilter(request, response);
    }
}
