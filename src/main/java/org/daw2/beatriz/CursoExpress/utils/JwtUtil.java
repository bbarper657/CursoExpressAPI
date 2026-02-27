package org.daw2.beatriz.CursoExpress.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {
    // @Value("${jwt.secret}")
    // private String secretKeyFromProperties;

    @Autowired
    private KeyPair jwtKeyPair;

    private static final long JWT_EXPIRATION = 3600000; // 1 hora

    // private SecretKey getSigningKey() {
    // return Keys.hmacShaKeyFor(secretKeyFromProperties.getBytes());
    // }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtKeyPair.getPublic()) // Configura la clave para verificar la firma
                .build()
                .parseSignedClaims(token) // Verifica el token y lo parsea
                .getPayload(); // Devuelve el cuerpo del JWT (claims)
    }

    public String generateToken(String username, List<String> roles, Long id) {
        return Jwts.builder()
                .subject(username) // Configura el claim "sub" (nombre de usuario)
                .claim("roles", roles) // Incluye los roles como claim adicional
                .claim("id", id) // Incluye el id como claim adicional
                .issuedAt(new Date()) // Fecha de emisión del token
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Expira en 1 hora
                .signWith(jwtKeyPair.getPrivate(), Jwts.SIG.RS256) // Firma el token con la clave secreta
                .compact(); // Genera el token en formato JWT
    }

    public boolean validateToken(String token, String username) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtKeyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return username.equals(claims.getSubject()) && !isTokenExpired(claims);
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
