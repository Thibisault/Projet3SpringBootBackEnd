package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JWTService;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private JWTService jwtService;

    @Autowired
    UserRepository userRepository;

    public AuthController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        Date actualDate = new Date();

        user.setCreated_at(actualDate);
        user.setUpdated_at(actualDate);

        UserEntity registeredUser = authService.registerUser(user);

        if (registeredUser != null){
            String token = jwtService.generateToken(registeredUser);
            return new ResponseEntity<>(("{ \"token\": \"" + token+"\" }"), HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        UserEntity authenticatedUser = authService.loginUser(email, password);
        if (authenticatedUser != null) {
            String token = jwtService.generateToken(authenticatedUser);
            return ResponseEntity.ok("{ \"token\": \"" + token+"\" }");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal!= null && principal instanceof JwtAuthenticationToken) {
            String email = ((JwtAuthenticationToken) principal).getName();
            return new ResponseEntity<>(userRepository.findByEmail(email), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
