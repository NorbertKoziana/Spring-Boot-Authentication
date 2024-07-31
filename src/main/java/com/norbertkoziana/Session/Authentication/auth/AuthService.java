package com.norbertkoziana.Session.Authentication.auth;
import com.norbertkoziana.Session.Authentication.model.LoginRequest;
import com.norbertkoziana.Session.Authentication.model.RegisterRequest;
import com.norbertkoziana.Session.Authentication.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface AuthService {

    void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
    void resendConfirmationMail(User user);
    void register(RegisterRequest registerRequest);
    Optional<User> findUserByEmail(String email);
}
