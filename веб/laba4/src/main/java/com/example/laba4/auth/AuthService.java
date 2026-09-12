package com.example.laba4.auth;

import com.example.laba4.auth.dto.AuthResponse;
import com.example.laba4.auth.dto.LoginRequest;
import com.example.laba4.auth.dto.RegisterRequest;
import com.example.laba4.db.tables.records.UsersRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest req) {
        var existing = userRepository.findByUsername(req.getUsername());
        if (existing.isPresent()) {
            return new AuthResponse(false, req.getUsername(), "Пользователь с таким именем уже существует");
        }
        String hash = passwordEncoder.encode(req.getPassword());
        UsersRecord created = userRepository.create(req.getUsername(), hash);
        String token = jwtUtil.generateToken(created.getUsername());
        return new AuthResponse(true, created.getUsername(), "Регистрация успешна", token);
    }

    public AuthResponse login(LoginRequest req) {
        var userOpt = userRepository.findByUsername(req.getUsername());
        if (userOpt.isEmpty()) {
            return new AuthResponse(false, req.getUsername(), "Неверное имя пользователя или пароль");
        }
        UsersRecord user = userOpt.get();
        boolean match = passwordEncoder.matches(req.getPassword(), user.getPasswordHash());
        if (!match) {
            return new AuthResponse(false, req.getUsername(), "Неверное имя пользователя или пароль");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(true, user.getUsername(), "Успешный вход", token);
    }
}
