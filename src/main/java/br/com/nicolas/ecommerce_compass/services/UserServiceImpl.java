package br.com.nicolas.ecommerce_compass.services;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.nicolas.ecommerce_compass.dtos.Email;
import br.com.nicolas.ecommerce_compass.exceptions.DuplicateEntryException;
import br.com.nicolas.ecommerce_compass.exceptions.EntityValidationException;
import br.com.nicolas.ecommerce_compass.exceptions.InvalidRequestException;
import br.com.nicolas.ecommerce_compass.exceptions.ResourceNotFoundException;
import br.com.nicolas.ecommerce_compass.models.User;
import br.com.nicolas.ecommerce_compass.repositories.UserRepository;
import br.com.nicolas.ecommerce_compass.security.TokenService;
import br.com.nicolas.ecommerce_compass.services.interfaces.EmailService;
import br.com.nicolas.ecommerce_compass.services.interfaces.SaleService;
import br.com.nicolas.ecommerce_compass.services.interfaces.UserService;

public class UserServiceImpl implements UserService {

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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token inválido ou expirado"));
    }

    public String login(User user) {
        User userDB = this.findByEmail(user.getEmail());
        if (passwordEncoder.matches(user.getEmail(), userDB.getEmail())) {
            var usernamePassword = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            var auth = authManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            saleService.clearSalesCache();

            return token;
        }
        throw new InvalidRequestException("Usuário e/ou senha incorretos");
    }

    @Override
    public void register(User user) {
        if (!this.emailMatchesPattern(user.getEmail())) {
            throw new InvalidRequestException("Email em formato inválido. Ex.: email@email.com");
        }

        var userDB = this.findByEmail(user.getEmail());
        if (userDB != null) {
            throw new DuplicateEntryException("Email já existente");
        }

        String encryptedPasswd = passwordEncoder.encode(user.getPassword());
        var newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encryptedPasswd);
        newUser.setRole(user.getRole());

        userRepository.save(newUser);
    }

    @Override
    public void sendResetToken() {
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
    }

    @Override
    public void resetPassword(String token, String password) {
        User user = this.findByResetToken(token);

        if (password == null || password.isBlank()) {
            throw new EntityValidationException("A senha não pode ser vazia");
        }

        String encryptedPassword = passwordEncoder.encode(password);

        user.setPassword(encryptedPassword);
        user.setResetToken(null);
        userRepository.save(user);
    }

    private boolean emailMatchesPattern(String email) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}
