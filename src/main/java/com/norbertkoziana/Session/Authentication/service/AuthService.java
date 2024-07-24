package com.norbertkoziana.Session.Authentication.service;
import com.norbertkoziana.Session.Authentication.dto.LoginRequest;
import com.norbertkoziana.Session.Authentication.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    boolean login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);

    void register(RegisterRequest registerRequest);

    boolean emailAlreadyUsed(String email);
}
