package br.com.nicolas.ecommerce_compass.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.nicolas.ecommerce_compass.models.User;
import br.com.nicolas.ecommerce_compass.models.UserRole;

@Service
public class JwtService {

    @Autowired
    private JwtEncoder encoder;

    public String generateToken(User user) {
        Instant now = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        Instant expiration = now.plusSeconds(300L);

        var scopes = user.getRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("ecommerce")
                .subject(user.getEmail())
                .issuedAt(now)
                .expiresAt(expiration)
                .claim("scope", scopes)
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
