package com.norbertkoziana.Session.Authentication.user;

import com.norbertkoziana.Session.Authentication.model.ResetPasswordRequest;
import com.norbertkoziana.Session.Authentication.model.SetPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/password/reset")
    ResponseEntity<Void> passwordReset(@RequestBody ResetPasswordRequest resetPasswordRequest){
        try {
            userService.initializePasswordReset(resetPasswordRequest.getEmail());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/password/set")
    ResponseEntity<Void> passwordSet(@RequestBody SetPasswordRequest setPasswordRequest){
        try {
            userService.setNewPassword(setPasswordRequest);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
