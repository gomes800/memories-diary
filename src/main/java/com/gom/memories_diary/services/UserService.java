package com.gom.memories_diary.services;

import com.gom.memories_diary.model.User;
import com.gom.memories_diary.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof User)) {
            throw new RuntimeException("User not authenticated.");
        }

        var user = (User) auth.getPrincipal();
        return user.getId();
    }
}
