package br.com.nava.cooperfilme.dtos;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.nava.cooperfilme.entities.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto implements UserDetails{

    private UUID id;
    
    private String login;

    private String password;

    private String name;

    private String phone;

    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role.equals(Role.ADMIN)){
            return List.of(
                new SimpleGrantedAuthority(Role.ADMIN.name()), 
                new SimpleGrantedAuthority(Role.ANALYST.name()),
                new SimpleGrantedAuthority(Role.REVIEWER.name()),
                new SimpleGrantedAuthority(Role.APPROVER.name()),
                new SimpleGrantedAuthority(Role.CLIENT.name()));
        }else{
            return List.of(new SimpleGrantedAuthority(this.role.name()));
        }
    }

    @Override
    public String getUsername() {
        return login;
    }

    public static UserDto fromEntity(UserEntity entity){
        return UserDto.builder()
            .id(entity.getId())
            .login(entity.getLogin())
            .password(entity.getPassword())
            .name(entity.getName())
            .phone(entity.getPhone())
            .role(Role.fromText(entity.getRole()))
            .build();
    }
    
}
