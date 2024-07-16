package br.com.nicolas.ecommerce_compass.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.nicolas.ecommerce_compass.exceptions.StandardError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        var status = HttpServletResponse.SC_UNAUTHORIZED;
        response.setContentType("application/json");
        response.setStatus(status);

        StandardError err = new StandardError(status, "Unauthorized", "É necessário logar");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), err);
    }

}
