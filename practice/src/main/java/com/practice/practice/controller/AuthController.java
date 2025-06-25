package com.practice.practice.controller;

import com.practice.practice.entity.Student;
import com.practice.practice.repository.StudentRepository;
import com.practice.practice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final StudentRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          UserDetailsService userDetailsService,
                          StudentRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
            authManager.authenticate(auth); // may throw exception if wrong credentials

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtUtil.generateToken(userDetails.getUsername());
        } catch (Exception e) {
            return "Login failed: " + e.getMessage();
        }
    }


    @PostMapping("/register")
    public String register(@RequestBody Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        userRepository.save(student);
        return "User registered successfully";
    }
}
