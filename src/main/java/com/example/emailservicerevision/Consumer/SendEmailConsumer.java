package com.example.emailservicerevision.Consumer;

import com.example.emailservicerevision.dtos.SendEmailMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Authenticator;

import java.util.Properties;

@Service

public class SendEmailConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(
            id = "EmailConsumer",
            topics="handleEmail"
    )
    public void sendEmail(String message) throws JsonProcessingException {
        SendEmailMessageDto messageDto=objectMapper.readValue(message, SendEmailMessageDto.class);
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(messageDto.getFrom(), "xgyogpzdiycmvujr");
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session,messageDto.getTo(),messageDto.getSubject(), messageDto.getBody());
    }
}
