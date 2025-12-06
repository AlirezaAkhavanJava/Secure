package com.arcade.securityspecific.controller;

import com.arcade.securityspecific.entity.User;
import com.arcade.securityspecific.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/public/register")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/public/login")
    public String login(@RequestBody User user) {
        User u = userRepository.findByUsername(user.getUsername());
        if (u == null) {
            return "Failed";
        }
        return "Success";
    }
}
