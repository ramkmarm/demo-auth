package com.sample_authentication.security;

import com.sample_authentication.model.User;
import com.sample_authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private final HttpServletRequest request;
    @Autowired
    private MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> webUser = userRepository.findByEmailAndDeletedFalse(username);
        if (webUser.isEmpty()) {
            throw new UsernameNotFoundException("Invalid user");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(webUser.get().getEmail()).password(webUser.get().getPassword())
                .accountExpired(false).accountLocked(false)
                .authorities("ROLE_" + webUser.get().getRole())
                .credentialsExpired(false).disabled(false).build();
    }


    public User getUser(String username) throws UsernameNotFoundException {
        final Optional<User> webUser = userRepository.findByEmailAndDeletedFalse(username);
        if (webUser.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username");
        }
        return webUser.get();
    }

}
