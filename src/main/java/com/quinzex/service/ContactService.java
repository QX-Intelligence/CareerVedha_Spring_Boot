package com.quinzex.service;

import com.quinzex.dto.ContactRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class ContactService {

    @Value("${admin.email}")
    private String adminEmail;
    private final SpringTemplateEngine templateEngine;
    private final IemailService iemailService;

    public String submitContact(ContactRequestDto request) {
        if (request.getName() == null || request.getName().isBlank())
            throw new IllegalArgumentException("Name is required");

        if (request.getEmail() == null || request.getEmail().isBlank())
            throw new IllegalArgumentException("Email is required");

        if (request.getMessage() == null || request.getMessage().isBlank())
            throw new IllegalArgumentException("Message is required");

        if (request.getPhone() == null || request.getPhone().isBlank())
            throw new IllegalArgumentException("Phone is required");
        String subject = request.getSubject();
        if (subject == null || subject.isBlank()) {
            subject = "No Subject";
        }
        Context context = new Context();
        context.setVariable("name", request.getName());
        context.setVariable("email", request.getEmail());
        context.setVariable("phone", request.getPhone());
        context.setVariable("subject", subject);
        context.setVariable("message", request.getMessage());

        String htmlContent = templateEngine.process("contact-mail", context);

        iemailService.sendHtmlEmail(adminEmail, subject, htmlContent);
    return "Thank you for reaching out to us. Our team will get back to you as soon as possible.";

    }
}