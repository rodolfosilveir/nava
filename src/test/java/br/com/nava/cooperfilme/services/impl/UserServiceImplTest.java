package br.com.nava.cooperfilme.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.nava.cooperfilme.dtos.UserDto;
import br.com.nava.cooperfilme.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private String secret = "mySecretKey";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userServiceImpl, "secret", secret);
    }

    @Test
    @DisplayName("Deve gerar um token válido, metodo generateToken")
    void shouldReturnsGenerateValidTokenSuccessfulyGenerateToken() {
        UserDto user = UserDto.builder().login("testUser").build();

        String token = userServiceImpl.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        String subject = JWT.require(Algorithm.HMAC256(secret))
                            .withIssuer("auth-api")
                            .build()
                            .verify(token)
                            .getSubject();

        assertEquals("testUser", subject);
    }

    @Test
    @DisplayName("Deve lançar InternalError ao gerar token, metodo generateToken")
    void shouldThrowInternalErrorGenerateToken() {
        UserDto user = UserDto.builder().login("testUser").build();

        ReflectionTestUtils.setField(userServiceImpl, "secret", "");

        InternalError exception = assertThrows(InternalError.class, () -> {
            userServiceImpl.generateToken(user);
        });

        assertEquals("Erro na geração do token", exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar um token válido, metodo validateToken")
    void shouldValidateValidTokenSuccessfulyValidateToken() {
        UserDto user = UserDto.builder().login("testUser").build();

        String token = userServiceImpl.generateToken(user);
        String subject = userServiceImpl.validateToken(token);

        assertEquals("testUser", subject);
    }

    @Test
    @DisplayName("Deve retornar string vazia ao validar um token inválido, metodo validateToken")
    void shouldReturnEmptyStringSuccessfulyValidateToken() {
        String invalidToken = "invalidToken";

        String result = userServiceImpl.validateToken(invalidToken);

        assertEquals("", result);
    }
}
