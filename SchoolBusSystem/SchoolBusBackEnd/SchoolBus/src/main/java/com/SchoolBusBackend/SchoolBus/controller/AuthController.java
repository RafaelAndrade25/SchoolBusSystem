package com.SchoolBusBackend.SchoolBus.controller;

import com.SchoolBusBackend.SchoolBus.dto.AuthResponse;
import com.SchoolBusBackend.SchoolBus.dto.RegisterRequest;
import com.SchoolBusBackend.SchoolBus.security.FirebaseAuthService;
import com.SchoolBusBackend.SchoolBus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final FirebaseAuthService firebaseAuthService;

    @Autowired
    public AuthController(UserService userService, FirebaseAuthService firebaseAuthService) {
        this.userService = userService;
        this.firebaseAuthService = firebaseAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = userService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<AuthResponse> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body(new AuthResponse(false, "Token inválido", null));
            }

            String token = authHeader.substring(7);
            return ResponseEntity.ok(userService.verifyUserToken(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("/user-profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body(new AuthResponse(false, "Token inválido", null));
            }

            String token = authHeader.substring(7);
            return ResponseEntity.ok(userService.getUserProfile(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(false, e.getMessage(), null));
        }
    }
}