package br.com.nava.cooperfilme.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import br.com.nava.cooperfilme.api.UserApi;
import br.com.nava.cooperfilme.api.requests.LoginRequest;
import br.com.nava.cooperfilme.api.responses.DefaultResponse;
import br.com.nava.cooperfilme.api.responses.LoginResponse;
import br.com.nava.cooperfilme.dtos.UserDto;
import br.com.nava.cooperfilme.services.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @Override
    public ResponseEntity<DefaultResponse<LoginResponse>> login(LoginRequest request){
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(request.login(), request.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = userService.generateToken((UserDto) auth.getPrincipal());

        return ResponseEntity.ok(DefaultResponse.<LoginResponse>builder()
            .httpStatus(200)
            .resultData(LoginResponse.builder().token(token).build())
            .build());
    }
    
}
