package com.spring.utils;

import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
public class MailUtil {

    private JavaMailSender javaMailSender;
    private MailProperties mailProperties;

    public MailUtil(JavaMailSender javaMailSender, MailProperties mailProperties) {
        this.javaMailSender=javaMailSender;
        this.mailProperties=mailProperties;
    }

    public void sendMail(String to, String subject, String content) throws Exception {
        // 发送邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        String personal = mailProperties.getProperties().get("personal");
        helper.setFrom(mailProperties.getUsername(), personal);
        helper.setTo(to); // 收件邮箱
        helper.setSubject(subject); // 邮件主题
        helper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }

}
