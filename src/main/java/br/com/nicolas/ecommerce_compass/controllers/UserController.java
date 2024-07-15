package br.com.nicolas.ecommerce_compass.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.nicolas.ecommerce_compass.dtos.user.LoginRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.RegisterDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.UserResponseDTO;
import br.com.nicolas.ecommerce_compass.maps.UserMapper;
import br.com.nicolas.ecommerce_compass.services.interfaces.UserService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok()
                .body(UserMapper.fromTokenToResponseDTO(userService.login(UserMapper.fromLoginDTOToUser(dto))));
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO dto) {
        userService.register(UserMapper.fromRegisterDTOToUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    @PostMapping("/resetpassword/mail")
    public ResponseEntity<Void> sendResetToken() {
        userService.sendResetToken();
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping("/resetpassword")
    public ResponseEntity<Void> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password) {
        userService.resetPassword(token, password);
        return ResponseEntity.ok().build();
    }

}
