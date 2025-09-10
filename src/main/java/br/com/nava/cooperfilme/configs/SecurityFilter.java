package br.com.nava.cooperfilme.configs;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import lombok.Generated;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.nava.cooperfilme.services.UserService;
import lombok.RequiredArgsConstructor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Generated
public class SecurityFilter extends OncePerRequestFilter{

    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = this.recoverToken(request);
        if(Objects.nonNull(token)){
            String login = userService.validateToken(token);
            Optional<UserDetails> userOp = userService.findByLogin(login);
            if(userOp.isPresent()){
                UserDetails user = userOp.get();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(Objects.isNull(authHeader)){
            return null;
        }

        return authHeader.replace("Bearer", "").trim();
    }
}
