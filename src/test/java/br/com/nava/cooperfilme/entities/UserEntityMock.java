package br.com.nava.cooperfilme.entities;

public class UserEntityMock {

    public static UserEntity create(){
        return UserEntity.builder()
                .login("testUser")
                .password("testPassword")
                .role("admin")
                .build();
    }
    
}
