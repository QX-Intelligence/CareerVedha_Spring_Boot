package com.quinzex.service;

import com.quinzex.dto.LoginRequest;
import com.quinzex.dto.LoginResponse;
import com.quinzex.dto.RegisterUser;
import jakarta.servlet.http.HttpServletResponse;

public interface IRegistrationLogin {
    public  String  RegisterUser(RegisterUser registerUser);
    public LoginResponse login(LoginRequest loginResponse, HttpServletResponse response);
    public  LoginResponse refresh(String refreshToken,HttpServletResponse response);
    public String Logout(String RefreshToken,HttpServletResponse response);
    public String sendLoginOtp(String email);
}
