//package br.com.plataformafly.authapi.security.service;
//
//import br.com.plataformafly.authapi.security.filter.TokenAuthenticationFilter;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Encoders;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.security.SignatureException;
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//public class TokenService {
//
//    @Value("${jwt.secret}")
//    private String secret;
//    private final static Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
//
//    public boolean isTokenValid(String token) {
//        try {
//            parseJwsClaim(token, secret);
//            return true;
//        } catch (ExpiredJwtException | MalformedJwtException | SecurityException |
//                 SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
//            logger.error(e.getMessage());
//            return false;
//        }
//    }
//
//    public String getTokenId(String token) {
//        Claims body = parseJwsClaim(token, secret).getBody();
//        return body.getSubject();
//    }
//
//    public List<String> getTokenRoles(String token) {
//        Claims body = parseJwsClaim(token, secret).getBody();
//        return body.get("roles", List.class);
//    }
//
//    public String getTokenFromHeader(HttpServletRequest request) {
//        String token = request.getHeader("Authorization");
//        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
//            return token.substring(7, token.length());
//        }
//        return null;
//    }
//
//    public static String getAuthorization() {
//        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
//        return request.getHeader(HttpHeaders.AUTHORIZATION);
//    }
//
//    public String getTokenUsername() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return userDetails.getUsername();
//    }
//
//    private Jws<Claims> parseJwsClaim(String token, String secret) {
//        // Transforma o secret em base64, e manda os bytes para formar a key
//        SecretKey key = Keys.hmacShaKeyFor(Encoders.BASE64.encode(secret.getBytes()).getBytes(StandardCharsets.UTF_8));
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//    }
//}
