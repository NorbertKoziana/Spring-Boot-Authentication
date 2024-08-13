package com.norbertkoziana.Session.Authentication.user;
import com.norbertkoziana.Session.Authentication.confirmation.Confirmation;
import com.norbertkoziana.Session.Authentication.confirmation.ConfirmationRepository;
import com.norbertkoziana.Session.Authentication.confirmation.ConfirmationService;
import com.norbertkoziana.Session.Authentication.email.ChangePasswordEmailService;
import com.norbertkoziana.Session.Authentication.model.SetPasswordRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final ConfirmationService confirmationService;

    private final ConfirmationRepository confirmationRepository;

    private final PasswordEncoder passwordEncoder;

    private final ChangePasswordEmailService changePasswordEmailService;

    @Override
    @Transactional
    public void initializePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow( () -> new IllegalStateException("User not found"));

        String token = UUID.randomUUID().toString();

        Confirmation confirmation = Confirmation.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .confirmed(false)
                .user(user)
                .build();

        confirmationRepository.save(confirmation);

        changePasswordEmailService.sendPasswordChangeMail(user.getEmail(), token);
    }
    @Override
    @Transactional
    public void setNewPassword(SetPasswordRequest setPasswordRequest) {
        Confirmation confirmation = confirmationService.checkToken(setPasswordRequest.getToken());

        if(!setPasswordRequest.getNewPassword().equals(setPasswordRequest.getRepeatPassword()))
            throw new IllegalStateException("Passwords are not equal.");

        confirmation.getUser().setPassword(passwordEncoder.encode(setPasswordRequest.getNewPassword()));
        confirmation.setConfirmed(true);
    }
    @Override
    @Transactional
    public Optional<User> block(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        user.ifPresent((user1) -> user1.setLocked(true));

        return user;
    }

}
