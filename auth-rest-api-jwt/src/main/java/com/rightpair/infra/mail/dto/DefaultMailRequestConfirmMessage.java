package com.rightpair.infra.mail.dto;

import com.rightpair.domain.users.exception.UserRegisterMessageException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public record DefaultMailRequestConfirmMessage (
        MimeMessage mimeMessage
) {
    private final static String MAIL_REGISTER_CONFIRM_SENDER = "noreply";
    private final static String MAIL_REGISTER_CONFIRM_SUBJECT = "회원 인증 요청 메일";
    public static final String MAIL_REGISTER_CONFIRM_URI = "/v1/users/confirm";

    public static DefaultMailRequestConfirmMessage from(
            JavaMailSender javaMailSender, String email, String content) {
        return new DefaultMailRequestConfirmMessage(generateMessage(javaMailSender, email, content));
    }

    private static MimeMessage generateMessage(
            JavaMailSender javaMailSender, String email, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(MAIL_REGISTER_CONFIRM_SENDER);
            helper.setTo(email);
            helper.setSubject(MAIL_REGISTER_CONFIRM_SUBJECT);
            helper.setText(content, true);
            javaMailSender.send(message);
            return message;
        } catch (MessagingException e) {
            throw new UserRegisterMessageException(e);
        }
    }

    public static String getRegisterConfirmUriString(String code) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(MAIL_REGISTER_CONFIRM_URI).path(code).build().toUriString();
    }
}
