package br.com.nicolas.ecommerce_compass.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.nicolas.ecommerce_compass.exceptions.InvalidTokenException;
import br.com.nicolas.ecommerce_compass.models.User;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.create()
                    .withIssuer("ecommerce")
                    .withSubject(user.getEmail())
                    .withIssuedAt(Instant.now().atZone(ZoneId.of("UTC")).toInstant())
                    .withExpiresAt(this.generateExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new InvalidTokenException("Erro ao criar token JWT");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            return JWT.require(algorithm)
                    .withIssuer("ecommerce")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    private Instant generateExpiration() {
        return LocalDateTime.now().plusMinutes(15).atZone(ZoneId.of("UTC")).toInstant();
    }
}
