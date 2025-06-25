package com.practice.practice.controller;

import com.practice.practice.dto.LoginRequest;
import com.practice.practice.entity.Student;
import com.practice.practice.exception.InvalidCredentialsException;
import com.practice.practice.repository.StudentRepository;
import com.practice.practice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public String login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword());
            authManager.authenticate(auth); // May throw BadCredentialsException

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            return jwtUtil.generateToken(userDetails.getUsername());
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid username or password");
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }



    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Student student) {
        if (student.getUsername() == null || student.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and password must not be null");
        }

        // Check if username already exists
        Optional<Student> existingUser = userRepository.findByUsername(student.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists. Please choose another.");
        }

        // Encode and save
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        userRepository.save(student);

        return ResponseEntity.ok("User registered successfully");
    }

}
