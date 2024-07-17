package br.com.nicolas.ecommerce_compass.services;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.nicolas.ecommerce_compass.dtos.Email;
import br.com.nicolas.ecommerce_compass.dtos.user.LoginRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.RegisterDTO;
import br.com.nicolas.ecommerce_compass.exceptions.DuplicateEntryException;
import br.com.nicolas.ecommerce_compass.exceptions.InvalidRequestException;
import br.com.nicolas.ecommerce_compass.exceptions.ResourceNotFoundException;
import br.com.nicolas.ecommerce_compass.exceptions.UnauthorizedOperationException;
import br.com.nicolas.ecommerce_compass.models.User;
import br.com.nicolas.ecommerce_compass.models.enums.UserRole;
import br.com.nicolas.ecommerce_compass.repositories.UserRepository;
import br.com.nicolas.ecommerce_compass.security.TokenService;
import br.com.nicolas.ecommerce_compass.services.interfaces.EmailService;
import br.com.nicolas.ecommerce_compass.services.interfaces.SaleService;
import br.com.nicolas.ecommerce_compass.services.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    // @Autowired
    // private AuthenticationManager authManager;

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

    @Override
    public String login(LoginRequestDTO dto) {
        User userDB = this.findByEmail(dto.email());
        if (passwordEncoder.matches(dto.password(), userDB.getPassword())) {
            var token = tokenService.generateToken(userDB);

            saleService.clearSalesCache();

            return token;
        }
        throw new InvalidRequestException("Usuário e/ou senha incorretos");
    }

    @Override
    public void register(RegisterDTO dto) {
        if (!this.emailMatchesPattern(dto.email())) {
            throw new InvalidRequestException("Email em formato inválido. Ex.: email@email.com");
        }
        if (!this.passwordMatchesPattern(dto.password())) {
            throw new InvalidRequestException(
                    "A senha precisa ter no mínimo 8 carácteres, uma letra maiúscula, uma carácter especial e ao menos um dígito. Ex: SenhaForte123!");
        }

        userRepository.findByEmail(dto.email()).ifPresent(userDB -> {
            throw new DuplicateEntryException("Email já existente");
        });

        String encryptedPasswd = passwordEncoder.encode(dto.password());
        var newUser = new User();
        newUser.setEmail(dto.email());
        newUser.setPassword(encryptedPasswd);
        newUser.setRole(dto.role());

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(user.getEmail().equals(auth.getName()) || this.isAdmin())) {
            throw new UnauthorizedOperationException("Apenas o próprio usuário ou ADMINs podem resetar a senha");
        }

        if (!this.passwordMatchesPattern(password)) {
            throw new InvalidRequestException(
                    "A senha precisa ter no mínimo 8 carácteres, uma letra maiúscula, uma carácter especial e ao menos um dígito. Ex: SenhaForte123!");
        }

        if (this.isAdmin()) {
            String subject = "Reset de Senha - Nova Senha";
            String text = "Olá " + user.getEmail() + ", \n\n"
                    + "Segue sua nova senha: " + password + "\n\n"
                    + "Lembre-se de redefini-la assim que entrar em nossos serviços";
            emailService.sendEmail(new Email(user.getEmail(), subject, text));
        }

        String encryptedPassword = passwordEncoder.encode(password);

        user.setPassword(encryptedPassword);
        user.setResetToken(null);
        userRepository.save(user);
    }

    private boolean emailMatchesPattern(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{3,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private boolean passwordMatchesPattern(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }
}
