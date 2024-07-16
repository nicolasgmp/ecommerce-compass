package br.com.nicolas.ecommerce_compass.services.interfaces;

import br.com.nicolas.ecommerce_compass.dtos.user.LoginRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.RegisterDTO;

public interface UserService {
    String login(LoginRequestDTO dto);

    void register(RegisterDTO dto);

    void sendResetToken();

    void resetPassword(String token, String password);
}
