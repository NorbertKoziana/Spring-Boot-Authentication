package com.norbertkoziana.Session.Authentication.email;
@FunctionalInterface
public interface ChangePasswordEmailService {
    void sendPasswordChangeMail(String emailAddress, String token);
}
