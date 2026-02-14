package com.quinzex.service;

public interface IotpService {

    public String generateOtp(String email);

    public boolean validateOtp(String email , String enteredOtp);

    String generateLoginOtp(String email);
    boolean validateLoginOtp(String email, String otp);
}
