package com.norbertkoziana.Session.Authentication.dto;

import lombok.Data;
@Data
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
