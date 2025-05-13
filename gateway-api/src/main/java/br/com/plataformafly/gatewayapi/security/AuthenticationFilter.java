package br.com.plataformafly.gatewayapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/auth/login",
            "/actuator", "/actuator/", "/actuator/**",
            "/swagger-ui", "/swagger-ui/**", "/v3/api-docs/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isPathExcluded(path)) {
            return chain.filter(exchange);
        }

        String token = jwtUtil.extractToken(exchange);

        if (token == null || !jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Aqui você pode adicionar os headers personalizados ou qualquer outra lógica necessária caso o token seja válido
        String username = jwtUtil.getUsername(token);
        List<String> roles = jwtUtil.getRoles(token);

        exchange.getRequest().mutate()
                .header("X-User", username)
                .header("X-Roles", String.join(",", roles));

        return chain.filter(exchange);
    }

    public boolean isPathExcluded(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public int getOrder() {
        return -1; // para ser executado antes do filtro padrão
    }
}
