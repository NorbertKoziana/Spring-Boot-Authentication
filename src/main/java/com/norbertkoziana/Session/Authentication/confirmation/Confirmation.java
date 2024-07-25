package com.norbertkoziana.Session.Authentication.confirmation;

import com.norbertkoziana.Session.Authentication.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ConfirmationToken")
public class Confirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    private LocalDateTime expiresAt;

    private Boolean confirmed;

    @ManyToOne
    @JoinColumn
    private User user;
}
