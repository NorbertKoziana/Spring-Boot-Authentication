package com.norbertkoziana.Session.Authentication.auth;
import com.norbertkoziana.Session.Authentication.dto.LoginRequest;
import com.norbertkoziana.Session.Authentication.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);

    void register(RegisterRequest registerRequest);

    boolean emailAlreadyUsed(String email);

}
