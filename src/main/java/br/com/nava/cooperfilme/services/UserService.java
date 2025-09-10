package br.com.nava.cooperfilme.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import br.com.nava.cooperfilme.dtos.UserDto;

public interface UserService {

    String validateToken(String token);

    String generateToken(UserDto user);

    Optional<UserDetails> findByLogin(String login);
    
}
