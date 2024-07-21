package com.norbertkoziana.Session.Authentication.repository;
import com.norbertkoziana.Session.Authentication.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE email=?1")
    Optional<User> findByEmail(String email);
}
