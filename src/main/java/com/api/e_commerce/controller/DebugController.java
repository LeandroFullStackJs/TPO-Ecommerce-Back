package com.api.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:5174"})
public class DebugController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/hash-password")
    public Map<String, String> hashPassword(@RequestBody Map<String, String> request) {
        String plainPassword = request.get("password");
        String hashedPassword = passwordEncoder.encode(plainPassword);
        
        Map<String, String> response = new HashMap<>();
        response.put("plainPassword", plainPassword);
        response.put("hashedPassword", hashedPassword);
        
        return response;
    }
}