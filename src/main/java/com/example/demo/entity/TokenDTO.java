package com.example.demo.entity;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TokenDTO {
    private String token = "token";
    private String generateToken = "";

    public Map<String, String> getTokenMap() {
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(token, generateToken);
        return tokenMap;
    }
}
