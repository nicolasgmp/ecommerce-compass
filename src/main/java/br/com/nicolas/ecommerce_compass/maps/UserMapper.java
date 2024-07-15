package br.com.nicolas.ecommerce_compass.maps;

import br.com.nicolas.ecommerce_compass.dtos.user.LoginRequestDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.RegisterDTO;
import br.com.nicolas.ecommerce_compass.dtos.user.UserResponseDTO;
import br.com.nicolas.ecommerce_compass.models.User;

public class UserMapper {
    private UserMapper() {
    }

    public static UserResponseDTO fromTokenToResponseDTO(String token) {
        return new UserResponseDTO(token);
    }

    public static User fromLoginDTOToUser(LoginRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        return user;
    }

    public static User fromRegisterDTOToUser(RegisterDTO dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(dto.role());
        return user;
    }
}
