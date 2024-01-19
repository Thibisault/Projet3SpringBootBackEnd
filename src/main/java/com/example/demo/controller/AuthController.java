package com.example.demo.controller;

import com.example.demo.entity.TokenDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JWTService;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api/auth")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "auth", description = "API pour gerer les utilisateurs")
public class AuthController {

    @Autowired
    private AuthService authService;

    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    public AuthController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Operation(summary = "Créer un compte")
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserEntity user) {

        UserEntity registeredUser = authService.registerUser(user);
        if (registeredUser != null){
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setGenerateToken(jwtService.generateToken(registeredUser));
            return new ResponseEntity<>(tokenDTO.getTokenMap(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "se connecter")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        UserEntity authenticatedUser = authService.loginUser(email, password);
        if (authenticatedUser != null) {

            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setGenerateToken(jwtService.generateToken(authenticatedUser));
            return new ResponseEntity<>(tokenDTO.getTokenMap(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Récupérer les informations relative en base de donnée de l'utilisateur courant")
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
