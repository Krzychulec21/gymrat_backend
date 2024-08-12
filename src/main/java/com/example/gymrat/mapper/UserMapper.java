package com.example.gymrat.mapper;

import com.example.gymrat.DTO.User.auth.RegisterRequest;
import com.example.gymrat.model.AuthProvider;
import com.example.gymrat.model.Role;
import com.example.gymrat.model.User;

public class UserMapper {

    public static User toEntity(RegisterRequest userCreateDTO) {
        User user = new User();
        user.setFirstName(userCreateDTO.firstName());
        user.setLastName(userCreateDTO.lastName());
        user.setNickname(userCreateDTO.nickname());
        user.setEmail(userCreateDTO.email());
        user.setPassword(userCreateDTO.password());
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setRole(Role.USER);
        return user;
    }


}
