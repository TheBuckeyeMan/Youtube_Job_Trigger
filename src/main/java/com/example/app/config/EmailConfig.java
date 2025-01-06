package com.example.app.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
    private static Logger log = LoggerFactory.getLogger(EmailConfig.class);
    
    @Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.password}")
    private String emailkey;

    @Value("${spring.mail.host}")
    private String smtp;

    @Bean
    public JavaMailSender getJavaMailSender() {
        System.out.println("EMAIL CHECK ENV: " + System.getenv("EMAIL"));
        System.out.println("EMAILKEY CHECK ENV: " + System.getenv("EMAIL_KEY"));
        System.out.println("HOST CHECK ENV: " + System.getenv("EMAIL_HOST")); //check for the env variable
        System.out.println("EMAIL CHECK PROP: " + email);
        System.out.println("EMAILKEY CHECK PROP: " + emailkey);
        System.out.println("HOST CHECK PROP: " + smtp); //check for the env variable
        log.info("Logger EMAIL CHECK PROP: " + email);
        log.info("Logger EMAIL CHECK PROP: " + emailkey);
        log.info("Logger EMAIL CHECK PROP: " + smtp);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtp);
        mailSender.setPort(587);
        
        mailSender.setUsername(email);
        mailSender.setPassword(emailkey);  // Replace with your Gmail App Password
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
