package com.flickcrit.app.infrastructure.security.service.impl;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.user.Email;
import com.flickcrit.app.domain.model.user.User;
import com.flickcrit.app.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {

    private static final String AUTHORITY_PREFIX = "ROLE_";

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = fetchUserOrThrow(username);
        return createUserDetails(user);
    }

    private UserDetails createUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail().value())
            .authorities(createAuthorities(user))
            .password(user.getPassword())
            .build();
    }

    private User fetchUserOrThrow(String username) throws UsernameNotFoundException {
        try {
            return userService.getUser(Email.of(username));
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }

    private Set<GrantedAuthority> createAuthorities(User user) {
        return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(AUTHORITY_PREFIX + role.name()))
            .collect(Collectors.toSet());
    }
}
