package br.com.nicolas.ecommerce_compass.services.interfaces;

import br.com.nicolas.ecommerce_compass.dtos.Email;

public interface EmailService {

    void sendEmail(Email email);
}
