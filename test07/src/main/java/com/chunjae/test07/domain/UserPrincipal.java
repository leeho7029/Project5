package com.chunjae.test07.domain;

import com.chunjae.test07.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

//UserDetails(스프링시큐리티에서 기본적으로 제공하는 인터페이스)
@Data
public class UserPrincipal implements UserDetails {
    private User user;
    public UserPrincipal(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new UserGrant());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    //계정이 없냐? true- 계정이 있따. false- 계정이 없다
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getActive() == 1;
    }

    public String getId(){
        return user.getLoginId();
    }

    public String getName(){
        return user.getUserName();
    }
}
