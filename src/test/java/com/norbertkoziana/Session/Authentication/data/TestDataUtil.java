package com.norbertkoziana.Session.Authentication.data;
import com.norbertkoziana.Session.Authentication.confirmation.Confirmation;
import com.norbertkoziana.Session.Authentication.model.LoginRequest;
import com.norbertkoziana.Session.Authentication.model.RegisterRequest;
import com.norbertkoziana.Session.Authentication.user.Role;
import com.norbertkoziana.Session.Authentication.user.User;

import java.time.LocalDateTime;
public final class TestDataUtil {

    public static Confirmation getConfirmationA(){
        return Confirmation.builder()
                .id(1)
                .token("2a09e71c-9a66-4c1d-a3d1-c3b024cc5362")
                .expiresAt(LocalDateTime.now().plusMinutes(20))
                .confirmed(true)
                .user(getUserA())
                .build();
    }

    public static Confirmation getConfirmationB(){
        return Confirmation.builder()
                .id(2)
                .token("7cdff7fd-91d3-421d-86f1-62407e43f2c9")
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .confirmed(false)
                .user(getUserB())
                .build();
    }

    public static Confirmation getConfirmationC(){
        return Confirmation.builder()
                .id(3)
                .token("435d4fb0-9ea2-4060-98b5-a4c9765988d7")
                .expiresAt(LocalDateTime.now().plusMinutes(12))
                .confirmed(false)
                .user(getUserA())
                .build();
    }

    public static Confirmation getConfirmationD(){
        return Confirmation.builder()
                .id(4)
                .token("bb8cd111-5dcb-46b8-9d77-24f8e737afaf")
                .expiresAt(LocalDateTime.now().plusMinutes(3))
                .confirmed(false)
                .user(getUserA())
                .build();
    }

    public static Confirmation getConfirmationE(){
        return Confirmation.builder()
                .id(5)
                .token("c84f333e-0334-4a8b-9239-81a68fe85cf9")
                .expiresAt(LocalDateTime.now().minusMinutes(2))
                .confirmed(false)
                .user(getUserA())
                .build();
    }

    public static User getUserA() {
        return User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("johhny@gmail.com")
                .password("123456")
                .locked(false)
                .enabled(true)
                .role(Role.User)
                .build();
    }

    public static User getUserB() {
        return User.builder()
                .id(2)
                .firstName("Willy")
                .lastName("Wonka")
                .email("Willies@gmail.com")
                .password("qwerty")
                .locked(false)
                .enabled(false)
                .role(Role.User)
                .build();
    }

    public static User getUserC() {
        return User.builder()
                .id(3)
                .firstName("Draco")
                .lastName("Malfoy")
                .email("drake@gmail.com")
                .password("zaq1@WSX")
                .locked(true)
                .enabled(true)
                .role(Role.User)
                .build();
    }

    public static String getTokenA() {
        return "a82f333e-0335-4a8b-9239-31a68fe85cf9";
    }

    public static LoginRequest getLoginRequestA(){
        return LoginRequest.builder()
                .email("johhny@gmail.com")
                .password("123456")
                .build();
    }

    public static RegisterRequest getRegisterRequestA(){
        return RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johhny@gmail.com")
                .password("123456")
                .build();
    }
}
