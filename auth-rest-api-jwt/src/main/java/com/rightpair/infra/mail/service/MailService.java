package com.rightpair.infra.mail.service;


import com.rightpair.infra.mail.dto.DefaultMailRequestConfirmMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
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

    public void sendRegisterConfirmMail(String email, String name, String code) {
        DefaultMailRequestConfirmMessage message = DefaultMailRequestConfirmMessage.from(
                javaMailSender,
                email,
                templateEngine.process("register-confirm", new Context(
                        Locale.KOREAN, Map.of(
                        "name", name,
                        "register_confirm_url", DefaultMailRequestConfirmMessage.getRegisterConfirmUriString(code)
                )))
        );
        javaMailSender.send(message.mimeMessage());
    }
}
