package br.com.nicolas.ecommerce_compass.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.nicolas.ecommerce_compass.dtos.Email;
import br.com.nicolas.ecommerce_compass.services.interfaces.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Email email) {
        var msg = new SimpleMailMessage();
        msg.setFrom("noreplyecommerce@email.com");
        msg.setTo(email.to());
        msg.setSubject(email.subject());
        msg.setText(email.body());
        mailSender.send(msg);
    }
}
