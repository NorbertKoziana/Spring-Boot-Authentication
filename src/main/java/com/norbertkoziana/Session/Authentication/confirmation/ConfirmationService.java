package com.norbertkoziana.Session.Authentication.confirmation;
import com.norbertkoziana.Session.Authentication.user.User;
public interface ConfirmationService {
    Confirmation checkToken(String token);
    void confirmEmail(String token);
}
