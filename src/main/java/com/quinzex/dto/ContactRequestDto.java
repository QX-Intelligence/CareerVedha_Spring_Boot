package com.quinzex.dto;

import lombok.Data;

@Data
public class ContactRequestDto {

    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
}
