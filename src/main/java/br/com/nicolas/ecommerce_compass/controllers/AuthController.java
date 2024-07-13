package br.com.nicolas.ecommerce_compass.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nicolas.ecommerce_compass.dtos.user.LoginRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.UserRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.UserResponseDTO;
import br.com.nicolas.ecommerce_compass.exceptions.InvalidRequestException;
import br.com.nicolas.ecommerce_compass.exceptions.ResourceNotFoundException;
import br.com.nicolas.ecommerce_compass.models.Role;
import br.com.nicolas.ecommerce_compass.models.User;
import br.com.nicolas.ecommerce_compass.repositories.UserRepository;
import br.com.nicolas.ecommerce_compass.security.TokenService;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (passwordEncoder.matches(dto.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok().body(new UserResponseDTO(token));
        }
        throw new InvalidRequestException("Usuário e/ou senha incorretos");
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO dto) {
        userRepository.findByEmail(dto.email()).ifPresent(user -> new InvalidRequestException("Email já cadastrado"));

        User newUser = new User();
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setRoles(dto.roles().stream()
                .filter(roleDTO -> Role.Values.exists(roleDTO.role()))
                .map(roleDTO -> {
                    Role role = new Role();
                    role.setName(roleDTO.role().toUpperCase());
                    return role;
                })
                .collect(Collectors.toSet()));
        userRepository.save(newUser);
        String token = tokenService.generateToken(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(token));
    }
}
