package com.norbertkoziana.Session.Authentication.confirmation;
public interface ConfirmationService {
    Confirmation checkToken(String token);
    void confirmEmail(String token);
    boolean checkIfConfirmationExpiryTimeIsAtLeast5Minutes(Confirmation confirmation);
}
