package com.quinzex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String email;

    private String firstName;

    private String lastName;

    private Boolean status;

    private String role;
}
