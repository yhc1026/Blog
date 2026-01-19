package com.spring.config;

import com.spring.utils.MailUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {

    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    @Bean
    public MailUtil mail(JavaMailSender javaMailSender, MailProperties mailProperties){
        return new MailUtil(javaMailSender, mailProperties);
    }

}
