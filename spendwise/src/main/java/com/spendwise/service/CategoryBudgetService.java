package com.spendwise.service;

// import javax.security.auth.login.AccountNotFoundException;

import org.springframework.stereotype.Service;

import com.spendwise.dto.CategoryBudgetCreationRequest;
import com.spendwise.dto.CategoryBudgetCreationResponse;
import com.spendwise.exception.customException.AccountNotFoundException;
import com.spendwise.exception.customException.CategoryNotFoundException;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.model.CategoryBudget;
import com.spendwise.repository.AccountRepository;
import com.spendwise.repository.CategoryBudgetRepository;
import com.spendwise.repository.CategoryRepository;
import com.spendwise.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryBudgetService {
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    public CategoryBudgetCreationResponse create(CategoryBudgetCreationRequest request, String email) {
        var user=userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException());
        var account = accountRepository
        .findByAccountIdAndUser(request.accountId(),user)
        .orElseThrow(()->new AccountNotFoundException());
        var category= categoryRepository.findById(request.categoryId()).orElseThrow(()->new CategoryNotFoundException());
        var newCategoryBudget = CategoryBudget.builder()
                .account(account)
                .category(category)
                .maxAllotedBudget(request.maxAllotedBudget())
                .name(request.name())
                .build();
        var savedCategoryBudget=categoryBudgetRepository.save(newCategoryBudget);
        return CategoryBudgetCreationResponse.builder()
                .categoryBudgetId(savedCategoryBudget.getCategoryBudgetId())
                .maxAllotedBudget(savedCategoryBudget.getMaxAllotedBudget())
                .name(savedCategoryBudget.getName())
                .build();
    }

}
