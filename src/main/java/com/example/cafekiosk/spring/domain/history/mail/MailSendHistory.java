package com.example.cafekiosk.spring.domain.history.mail;

import com.example.cafekiosk.spring.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailSendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromEmail;
    private String toEmail;
    private String subject;
    private String content;

    @Builder
    public MailSendHistory(String fromEmail, String toEmail, String subject, String content) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.content = content;
    }
}