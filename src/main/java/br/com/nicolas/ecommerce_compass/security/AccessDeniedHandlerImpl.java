package br.com.nicolas.ecommerce_compass.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.nicolas.ecommerce_compass.exceptions.StandardError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        var status = HttpServletResponse.SC_FORBIDDEN;
        response.setContentType("application/json");
        response.setStatus(status);

        StandardError err = new StandardError(status, "Forbidden", "Acesso Negado");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), err);
    }

}
