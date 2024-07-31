package com.norbertkoziana.Session.Authentication.confirmation;

import com.norbertkoziana.Session.Authentication.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Integer> {
    Optional<Confirmation> findByToken(String token);

    Optional<Confirmation> findFirstByUserOrderByExpiresAtDesc(User user);
}
