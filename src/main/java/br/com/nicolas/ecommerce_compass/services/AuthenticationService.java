package br.com.nicolas.ecommerce_compass.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.nicolas.ecommerce_compass.models.User;

@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    public String authenticate(User user) {
        return jwtService.generateToken(user);
    }
}
