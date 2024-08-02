package com.norbertkoziana.Session.Authentication.mapper.impl;
import com.norbertkoziana.Session.Authentication.mapper.Mapper;
import com.norbertkoziana.Session.Authentication.model.UserDto;
import com.norbertkoziana.Session.Authentication.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<User, UserDto> {

    private final ModelMapper modelMapper;

    @Override
    public UserDto mapTo(User user) {
        return modelMapper.map(user, UserDto.class);
    }
    @Override
    public User mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
