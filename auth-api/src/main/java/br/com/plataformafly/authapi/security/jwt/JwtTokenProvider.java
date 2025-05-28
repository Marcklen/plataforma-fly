package br.com.plataformafly.authapi.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class JwtTokenProvider {

    @Value("${auth.jwt.secret}")
    private String jwtSecret;

    @Value("${auth.jwt.expiration}")
    private long jwtExpiration;

    private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);


    public String generateToken(UserDetails userDetails, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000); // segundos → ms

        SecretKey key = getSigningKey();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            parseJwsClaim(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SecurityException |
                 UnsupportedJwtException | IllegalArgumentException e) {
            log.error("Erro ao validar token: {}", e.getMessage());
            return false;
        }
    }

    public String getTokenUsername(String token) {
        return parseJwsClaim(token).getBody().getSubject();
    }

    public List<String> getTokenRoles(String token) {
        return parseJwsClaim(token).getBody().get("roles", List.class);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public static String getAuthorizationHeader() {
//        return TokenService.getAuthorization();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private Jws<Claims> parseJwsClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
