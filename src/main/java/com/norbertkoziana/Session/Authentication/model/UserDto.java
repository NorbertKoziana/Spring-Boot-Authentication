package com.norbertkoziana.Session.Authentication.model;

import com.norbertkoziana.Session.Authentication.user.Role;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
    private Role role;

}
