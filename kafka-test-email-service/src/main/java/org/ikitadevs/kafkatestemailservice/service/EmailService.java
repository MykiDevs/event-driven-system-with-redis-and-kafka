package org.ikitadevs.kafkatestemailservice.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.kafkatestemailservice.repository.EventRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;


    public void sendMail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("papapewegimamodi@test.com");
        message.setSubject(subject);
        message.setText(body);
        message.setTo(toEmail);
        javaMailSender.send(message);
    }

    public void sendWelcomeEmail(String toEmail, String body) {
        sendMail(toEmail, "Welcome to our site!", body);
    }

}
