package com.arcade.securityspecific.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;


    /*SETS THE RULES OF AUTHENTICATION AND AUTHORIZATION*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers("/public/**").permitAll()
                                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) /*GIVES A WAY TO ADD USERPASS */
                .formLogin(Customizer.withDefaults()) /*GIVES A FORM TO INPUT THE USERPASS*/
                .csrf(AbstractHttpConfigurer::disable); /*no csrf for now*/
        return http.build();
    }

    //@Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin").password("{noop}code").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user);
    }

    // USED TO ENCODE PASSWORDS AND IN PROVIDER ALSO
    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(14);
    }


    @Bean
    public AuthenticationProvider authenticationProvider(BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService); //user details
        provider.setPasswordEncoder(passwordEncoder); //password (encoded)
        provider.setHideUserNotFoundExceptions(true);
        return provider;
    }

}
