package com.example.RoomReservationSystem.service;

import com.example.RoomReservationSystem.repository.user_repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;

    @Async
    public void SendSuccessfulRequestMail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yasarbilgi.rezervasyon@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
       try{
           javaMailSender.send(message);
       }
       catch (Exception e) {
           System.out.println("Error sending mail: " + e.getMessage());
       }

       System.out.println("Mail sent successfully.");
    }

    @Async
    public void SendApprovedReservationMail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yasarbilgi.rezervasyon@gmail.com");
        message.setBcc(userRepository.findAnyReservationOfficerEmail());
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
       try{
           javaMailSender.send(message);
       }
       catch (Exception e) {
           System.out.println("Error sending mail: " + e.getMessage());
       }

       System.out.println("Mail sent successfully.");
    }


    @Async
    public void SendNotificationToReservationOfficerMail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yasarbilgi.rezervasyon@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
       try{
           javaMailSender.send(message);
       }
       catch (Exception e) {
           System.out.println("Error sending mail: " + e.getMessage());
       }

       System.out.println("Mail sent successfully.");
    }
}
