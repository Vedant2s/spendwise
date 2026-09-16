package com.spendwise.controller;

import org.springframework.web.bind.annotation.RestController;

import com.spendwise.dto.LoginRequest;
import com.spendwise.dto.LoginResponse;
import com.spendwise.service.LogInService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class LogInController {
    @Autowired
    LogInService logInService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> postMethodName(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(logInService.login(request));
    }

}
