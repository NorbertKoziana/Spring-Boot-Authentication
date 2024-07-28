package com.norbertkoziana.Session.Authentication.email;
@FunctionalInterface
public interface ConfirmationEmailService {
    void sendConfirmationMail(String emailAddress, String token);
}
