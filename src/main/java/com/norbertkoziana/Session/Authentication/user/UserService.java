package com.norbertkoziana.Session.Authentication.user;
import com.norbertkoziana.Session.Authentication.model.SetPasswordRequest;
public interface UserService {
    void initializePasswordReset(String email);
    void setNewPassword(SetPasswordRequest setPasswordRequest);
}
