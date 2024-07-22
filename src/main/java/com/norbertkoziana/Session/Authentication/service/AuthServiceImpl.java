package com.norbertkoziana.Session.Authentication.service;
import com.norbertkoziana.Session.Authentication.dto.LoginRequest;
import com.norbertkoziana.Session.Authentication.dto.RegisterRequest;
import com.norbertkoziana.Session.Authentication.repository.UserRepository;
import com.norbertkoziana.Session.Authentication.user.Role;
import com.norbertkoziana.Session.Authentication.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        //authenticate
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        //session management
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
    @Override
    public void register(RegisterRequest registerRequest) {
        //TODO: add validation

        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent())//TODO: move this if to controller and return reponse
            // entity instead of throwing exception
            throw new IllegalStateException("Email already used");

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .locked(false)
                .enabled(true)//TODO: change to false after allowing user to verify his email
                .role(Role.User)
                .build();

        userRepository.save(user);

    }
}
