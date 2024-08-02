package com.example.gymrat.mapper;

import com.example.gymrat.DTO.User.UserCreateDTO;
import com.example.gymrat.model.AuthProvider;
import com.example.gymrat.model.User;

public class UserMapper {

    public static User toEntity(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setFirstName(userCreateDTO.firstName());
        user.setLastName(userCreateDTO.lastName());
        user.setUsername(userCreateDTO.username());
        user.setEmail(userCreateDTO.email());
        user.setPassword(userCreateDTO.password());
        user.setAuthProvider(AuthProvider.LOCAL);
        return user;
    }


}
