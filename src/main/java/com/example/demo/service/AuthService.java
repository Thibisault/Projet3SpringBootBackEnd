package com.example.demo.service;


import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity registerUser(UserEntity user) {
        if (userRepository.findByEmail(user.getEmail()) == null){
            return userRepository.save(user);
        }
        return null;
    }

    public UserEntity loginUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal instanceof JwtAuthenticationToken) {
            String email = ((JwtAuthenticationToken) principal).getName();
            return userRepository.findByEmail(email);
        }
        return null;
    }

    public UserEntity getAuthenticatedUser() {

        return null;
    }
}
