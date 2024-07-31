package com.norbertkoziana.Session.Authentication.auth;
import com.norbertkoziana.Session.Authentication.model.LoginRequest;

import com.norbertkoziana.Session.Authentication.model.RegisterRequest;
import com.norbertkoziana.Session.Authentication.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest){
        Optional<User> userOptional = authService.findUserByEmail(registerRequest.getEmail());

        if(userOptional.isEmpty()){
            authService.register(registerRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        User user = userOptional.get();
        if(!user.getEnabled())
            authService.resendConfirmationMail(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
