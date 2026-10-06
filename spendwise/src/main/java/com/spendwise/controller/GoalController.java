package com.spendwise.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spendwise.dto.GoalCreationRequest;
import com.spendwise.dto.GoalCreationResponse;
import com.spendwise.service.GoalService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping ("/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;
    @PostMapping
    public ResponseEntity<GoalCreationResponse> createGoal(@RequestBody GoalCreationRequest request,Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.create(request,authentication.getName()));
    }

}
