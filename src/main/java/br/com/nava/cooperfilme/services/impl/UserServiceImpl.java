package br.com.nava.cooperfilme.services.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.nava.cooperfilme.dtos.UserDto;
import br.com.nava.cooperfilme.repositories.UserRepository;
import br.com.nava.cooperfilme.services.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService{

    private final UserRepository userRepository;

    @Value("${api.security.token.security}")
    private String secret;

    @Override
    public String generateToken(UserDto user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.getLogin())
                .withExpiresAt(genExpirationDate())
                .sign(algorithm);
        }catch(Exception e){
            throw new InternalError("Erro na geração do token", e);
        }
    }

    @Override
    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getSubject();
        } catch(JWTVerificationException e){
            return "";
        }
    }

    @Override
    public Optional<UserDetails> findByLogin(String login) {
        return userRepository.findByLogin(login).map(UserDto::fromEntity);
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    @Override 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
            .map(UserDto::fromEntity)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario " + username + " não encontrado"));
    }
    
}
