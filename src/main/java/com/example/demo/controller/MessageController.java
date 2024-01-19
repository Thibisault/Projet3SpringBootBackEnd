package com.example.demo.controller;

import com.example.demo.entity.Message;
import com.example.demo.service.AuthService;
import com.example.demo.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api/messages")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "message", description = "API pour gerer la création de nouveaux messages")
public class MessageController {

    @Autowired
    private AuthService authService;

    @Autowired
    private MessageService messageService;

    @Operation(summary = "Créer un nouveau message", description = "Créer un nouveau message")
    @PostMapping
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody Message message) {

        Date actualDate = new Date();

        message.setCreated_at(actualDate);
        messageService.createMessage(message);

        Map<String, String> listMessage = new HashMap<>();
        listMessage.put("message", "Message send with success");
        return new ResponseEntity<>(listMessage, HttpStatus.OK);

    }

}