package com.quinzex.dto;

import lombok.Data;

@Data
public class RegisterUser {

    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String otp;
}



