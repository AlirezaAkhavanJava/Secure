package com.arcade.securityspecific.auth;

import com.arcade.securityspecific.entity.User;
import com.arcade.securityspecific.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class CustomUserDetailsService
        implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            log.error("Username {} not found", username);
            throw new UsernameNotFoundException("User not found");
        }

        /*UserDetailsService will return UserDetails*/
        return new CustomUserDetails(user);
    }
}
