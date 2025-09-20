package com.gom.memories_diary.services;

import com.gom.memories_diary.dto.AuthDTO;
import com.gom.memories_diary.dto.LoginResponseDTO;
import com.gom.memories_diary.dto.RegisterDTO;
import com.gom.memories_diary.model.User;
import com.gom.memories_diary.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(AuthDTO data) {
        var authToken = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        var auth = authenticationManager.authenticate(authToken);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }

    public void register(RegisterDTO data) {
        if (userRepository.findByUsername(data.getUsername()) != null) {
            throw new RuntimeException("User already exists.");
        }

        String encryptedPassword = passwordEncoder.encode(data.getPassword());
        User newUser = User.builder()
                .username(data.getUsername())
                .email(data.getEmail())
                .password(encryptedPassword)
                .build();
        userRepository.save(newUser);
    }
}
