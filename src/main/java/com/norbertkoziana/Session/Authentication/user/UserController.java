package com.norbertkoziana.Session.Authentication.user;

import com.norbertkoziana.Session.Authentication.mapper.Mapper;
import com.norbertkoziana.Session.Authentication.mapper.impl.UserMapper;
import com.norbertkoziana.Session.Authentication.model.ResetPasswordRequest;
import com.norbertkoziana.Session.Authentication.model.SetPasswordRequest;
import com.norbertkoziana.Session.Authentication.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final Mapper<User, UserDto> userMapper;

    @PostMapping("/password/reset")
    ResponseEntity<Void> passwordReset(@RequestBody ResetPasswordRequest resetPasswordRequest){
        try {
            userService.initializePasswordReset(resetPasswordRequest.getEmail());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/password/set")
    ResponseEntity<Void> passwordSet(@RequestBody SetPasswordRequest setPasswordRequest){
        try {
            userService.setNewPassword(setPasswordRequest);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/info")
    UserDto getUserInfo(@AuthenticationPrincipal User user){
        return userMapper.mapTo(user);
    }

    @PatchMapping("/{email}/block")
    ResponseEntity<UserDto> blockUser(@PathVariable("email") String email){
        Optional<User> user = userService.block(email);

        return user.map(
                (blockedUser) -> {
                    return new ResponseEntity<>(userMapper.mapTo(blockedUser), HttpStatus.OK);
                }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}