package br.com.nava.cooperfilme.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.com.nava.cooperfilme.api.requests.LoginRequest;
import br.com.nava.cooperfilme.api.responses.DefaultResponse;
import br.com.nava.cooperfilme.api.responses.LoginResponse;
import br.com.nava.cooperfilme.dtos.Role;
import br.com.nava.cooperfilme.dtos.UserDtoMock;
import br.com.nava.cooperfilme.services.UserService;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Deve retonar o Login com sucesso, metodo login")
    void shouldReturnsLoginSuccessfullyLogin(){
        String login = "login";
        String password = "password";
        String token = "token";
        Authentication authentication = new UsernamePasswordAuthenticationToken(UserDtoMock.create(login, UUID.randomUUID(), Role.ADMIN), password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);

        when(userService.generateToken(any())).thenReturn(token);

        ResponseEntity<DefaultResponse<LoginResponse>> response = userController.login(new LoginRequest(login, password));

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        assertNotNull(response.getBody());
    }
    
}
