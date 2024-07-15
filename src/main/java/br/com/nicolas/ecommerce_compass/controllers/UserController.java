package br.com.nicolas.ecommerce_compass.controllers;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.nicolas.ecommerce_compass.dtos.Email;
import br.com.nicolas.ecommerce_compass.dtos.user.LoginRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.RegisterDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.UserResponseDTO;
import br.com.nicolas.ecommerce_compass.exceptions.DuplicateEntryException;
import br.com.nicolas.ecommerce_compass.exceptions.EntityValidationException;
import br.com.nicolas.ecommerce_compass.exceptions.InvalidRequestException;
import br.com.nicolas.ecommerce_compass.exceptions.ResourceNotFoundException;
import br.com.nicolas.ecommerce_compass.models.User;
import br.com.nicolas.ecommerce_compass.repositories.UserRepository;
import br.com.nicolas.ecommerce_compass.security.TokenService;
import br.com.nicolas.ecommerce_compass.services.EmailService;
import br.com.nicolas.ecommerce_compass.services.interfaces.SaleService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (passwordEncoder.matches(dto.password(), user.getPassword())) {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            var auth = authManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            saleService.clearSalesCache();

            return ResponseEntity.ok().body(new UserResponseDTO(token));
        }

        throw new InvalidRequestException("Usuário e/ou senha incorretos");
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO dto) {
        if (!this.emailMatchesPattern(dto.email())) {
            throw new InvalidRequestException("Email em formato inválido. Ex.: email@email.com");
        }
        var user = userRepository.findByEmail(dto.email());

        if (user.isPresent()) {
            throw new DuplicateEntryException("Email já existente");
        }

        String encryptedPasswd = passwordEncoder.encode(dto.password());
        var newUser = new User();
        newUser.setEmail(dto.email());
        newUser.setPassword(encryptedPasswd);
        newUser.setRole(dto.role());

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    @PostMapping("/resetpassword/mail")
    public ResponseEntity<Void> sendResetToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        String token = UUID.randomUUID().toString();
        var user = userRepository.findByEmail(email).get();
        user.setResetToken(token);
        userRepository.save(user);

        String subject = "Reset de Senha - Ecommerce Compass Desafio";
        String text = "Olá " + email + ", \n\n"
                + "Para redefinir sua senha, utilize o seguinte token: " + token + "\n\n"
                + "Se você desconhece essa solicitação de redefinição, por favor desconsidere este e-mail.\n\n";

        emailService.sendEmail(new Email(email, subject, text));

        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping("/resetpassword")
    public ResponseEntity<Void> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token inválido ou expirado"));
        if (password == null || password.isBlank()) {
            throw new EntityValidationException("A senha não pode ser vazia");
        }
        String encryptedPasswd = passwordEncoder.encode(password);

        user.setPassword(encryptedPasswd);
        user.setResetToken(null);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    private boolean emailMatchesPattern(String email) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}
