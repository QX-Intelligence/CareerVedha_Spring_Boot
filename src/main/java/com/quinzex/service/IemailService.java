package com.quinzex.service;

import java.util.concurrent.CompletableFuture;

public interface IemailService {

    public void sendEmail(String email, String otp);
    public void sendLoginEmail(String email);
    public void sendSuspeciousEmail(String email);
    public void sendApprovalEmail(String email, String role);
    public void sendRejectionEmail(String email, String role, String reason);
    public void sendHtmlEmail(String to, String subject, String htmlContent);
}
