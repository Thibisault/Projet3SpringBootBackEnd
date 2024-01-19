package com.example.demo.controller;

import com.example.demo.entity.DTOuser;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/user")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "user", description = "API pour créer un objet dto à partir de la classe userEntity")
public class UserController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Créer un objet dtoUser", description = "Créer un objet dtoUser puis le retourne")
    @GetMapping("/{id}")
    public ResponseEntity<DTOuser> getUserById(@PathVariable Long id) {
        UserEntity userEntity = userRepository.getReferenceById(id);

        if (userEntity != null) {
            DTOuser dtOuser = new DTOuser();
            dtOuser.setId(userEntity.getId());
            dtOuser.setName(userEntity.getName());
            dtOuser.setEmail(userEntity.getEmail());
            dtOuser.setCreated_at(userEntity.getCreated_at());
            dtOuser.setUpdated_at(userEntity.getUpdated_at());
            return new ResponseEntity<>(dtOuser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
