package com.apps.trader.serivice;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
public class EmailService {



    private JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "Utf8");
        
        String subject = "Verify OTP";
        String text = " Your verification code is " + otp;

        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);
        mimeMessageHelper.setTo(email);

        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new MailSendException(e.getMessage());
        }

       
    }

}