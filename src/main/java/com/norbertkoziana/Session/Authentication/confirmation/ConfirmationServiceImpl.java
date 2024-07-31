package com.norbertkoziana.Session.Authentication.confirmation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationRepository confirmationRepository;

    public Confirmation checkToken(String token){
        Confirmation confirmation = confirmationRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found."));

        if(confirmation.getConfirmed())
            throw new IllegalStateException("Token already confirmed.");

        if(confirmation.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Token expired.");

        return confirmation;
    }

    @Override
    @Transactional
    public void confirmEmail(String token) {
        Confirmation confirmation = checkToken(token);

        confirmation.getUser().setEnabled(true);
        confirmation.setConfirmed(true);
    }

    public boolean checkIfConfirmationExpiryTimeIsAtLeast5Minutes(Confirmation confirmation){
        return confirmation.getExpiresAt().isAfter(LocalDateTime.now().plusMinutes(5));
    }
}
