package com.spendwise.controller;

import org.springframework.web.bind.annotation.RestController;

import com.spendwise.dto.LoginRequest;
// import com.spendwise.dto.LoginResponse;
import com.spendwise.dto.RegisterRequest;
import com.spendwise.dto.AuthenticationResponse;
import com.spendwise.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/auth/v1")
public class AuthController {
    final AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> postLoginCred(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> postRegistringCred(@RequestBody RegisterRequest request) {
        AuthenticationResponse response =authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
