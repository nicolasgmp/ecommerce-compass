package br.com.nicolas.ecommerce_compass.services.interfaces;

import br.com.nicolas.ecommerce_compass.models.User;

public interface UserService {
    String login(User user);

    void register(User user);

    void sendResetToken();

    void resetPassword(String token, String password);
}
