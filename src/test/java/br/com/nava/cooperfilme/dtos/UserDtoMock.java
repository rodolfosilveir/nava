package br.com.nava.cooperfilme.dtos;

import java.util.UUID;

public class UserDtoMock {

    public static UserDto create(String login, UUID uuid){
        return UserDto.builder()
            .id(uuid)
            .login(login)
            .password("password")
            .name("name")
            .phone("phone")
            .role(Role.ADMIN)
            .build();
    }
    
}
