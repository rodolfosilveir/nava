package br.com.nava.cooperfilme.dtos;

import java.util.UUID;

public class UserDtoMock {

    public static UserDto create(String login, UUID uuid, Role roleUser){
        return UserDto.builder()
            .id(uuid)
            .login(login)
            .password("password")
            .name("name")
            .phone("phone")
            .role(roleUser)
            .build();
    }
    
}
