package com.norbertkoziana.Session.Authentication.model;
import lombok.Data;
@Data
public class SetPasswordRequest {

    private String token;
    private String newPassword;
    private String repeatPassword;

}
