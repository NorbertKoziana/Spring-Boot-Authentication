package com.norbertkoziana.Session.Authentication.auth;
import com.norbertkoziana.Session.Authentication.dto.LoginRequest;

import com.norbertkoziana.Session.Authentication.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        try{
            authService.login(loginRequest, request, response);
        }catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest){
        if(authService.emailAlreadyUsed(registerRequest.getEmail()))
            return new ResponseEntity<>(HttpStatus.OK);

        authService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
