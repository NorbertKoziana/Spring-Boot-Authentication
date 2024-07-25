package com.norbertkoziana.Session.Authentication.confirmation;
public interface ConfirmationService {
    void confirmEmail(String token);
}
