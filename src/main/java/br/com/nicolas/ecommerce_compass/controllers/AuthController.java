package br.com.nicolas.ecommerce_compass.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nicolas.ecommerce_compass.dtos.user.LoginRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.UserResponseDTO;
import br.com.nicolas.ecommerce_compass.exceptions.ResourceNotFoundException;
import br.com.nicolas.ecommerce_compass.repositories.UserRepository;
import br.com.nicolas.ecommerce_compass.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        var user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Email e/ou Senha incorretos"));

        var token = authService.authenticate(user);
        return ResponseEntity.ok(new UserResponseDTO(token));
    }

    // @PostMapping("/register")
    // public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO
    // dto) {
    // authService.register(dto);
    // return ResponseEntity.status(HttpStatus.CREATED).build();
    // }
}
