package edu.utycc.reg.security;


import edu.utycc.reg.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .flatMap(role -> {
                    Collection<SimpleGrantedAuthority> roleAuthority = 
                        java.util.List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));

                    Collection<SimpleGrantedAuthority> permAuthority = role.getPermissions().stream()
                            .map(p -> new SimpleGrantedAuthority(p.getCode()))
                            .collect(Collectors.toSet());

                    return java.util.stream.Stream.concat(roleAuthority.stream(), permAuthority.stream());
                })
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != User.Status.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == User.Status.ACTIVE;
    }

    public User getUser(){
        return user;
    }
}
