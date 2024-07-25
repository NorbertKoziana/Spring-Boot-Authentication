package com.norbertkoziana.Session.Authentication.email;
public interface EmailService {
    void sendConfirmationMail(String emailAddress, String token);
}
