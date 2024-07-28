package com.norbertkoziana.Session.Authentication.user;
import com.norbertkoziana.Session.Authentication.model.SetPasswordRequest;
public interface UserService {
    boolean emailAlreadyUsed(String email);
    void initializePasswordReset(String email);
    void setNewPassword(SetPasswordRequest setPasswordRequest);
}
