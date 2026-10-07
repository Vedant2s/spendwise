package com.spendwise.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spendwise.dto.CategoryBudgetCreationRequest;
import com.spendwise.dto.CategoryBudgetCreationResponse;
import com.spendwise.service.CategoryBudgetService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping ("/budget")
@RequiredArgsConstructor
public class CategoryBudgetController {
    private final CategoryBudgetService categoryBudgetService;
    @PostMapping
    public ResponseEntity<CategoryBudgetCreationResponse> createBudget(@RequestBody CategoryBudgetCreationRequest request,Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryBudgetService.create(request,authentication.getName()));
    }

}
