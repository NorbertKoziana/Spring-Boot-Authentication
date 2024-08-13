package com.norbertkoziana.Session.Authentication.user;
import com.norbertkoziana.Session.Authentication.model.SetPasswordRequest;

import java.util.Optional;
public interface UserService {
    void initializePasswordReset(String email);
    void setNewPassword(SetPasswordRequest setPasswordRequest);
    Optional<User> block(String email);
}
