package com.rightpair.infra.mail.service;


import com.rightpair.domain.users.exception.UserRegisterMessageException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${service.mail.register.subject}")
    private String registerConfirmMailSubject;

    @Value("${service.mail.register.sender}")
    private String registerConfirmMailSender;

    @Value("${service.mail.register.url}")
    private String registerConfirmMailUrl;

    public void sendRegisterConfirmMail(String email, String name, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(registerConfirmMailSender);
            helper.setTo(email);
            helper.setSubject(registerConfirmMailSubject);
            helper.setText(generateMailTextFromRegisterTemplate(name, code), true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new UserRegisterMessageException(e);
        }
    }

    private String generateMailTextFromRegisterTemplate(String name, String code) {
        return templateEngine.process("register-confirm", new Context(
                Locale.KOREAN, Map.of(
                "name", name,
                "register_confirm_url", registerConfirmMailUrl.formatted(code)
        )));
    }
}
