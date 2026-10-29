package com.spendwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spendwise.config.Role;
import com.spendwise.dto.CategoryBudgetCreationRequest;
import com.spendwise.dto.CategoryBudgetCreationResponse;
import com.spendwise.dto.Type;
import com.spendwise.exception.customException.AccountNotFoundException;
import com.spendwise.exception.customException.CategoryNotFoundException;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.model.Account;
import com.spendwise.model.Category;
import com.spendwise.model.CategoryBudget;
import com.spendwise.model.Currency;
import com.spendwise.model.User;
import com.spendwise.repository.AccountRepository;
import com.spendwise.repository.CategoryBudgetRepository;
import com.spendwise.repository.CategoryRepository;
import com.spendwise.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryBudgetServiceTest {
    @Mock
    private CategoryBudgetRepository categoryBudgetRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryBudgetService categoryBudgetService;

    private Currency currency;
    private User user;
    private Account account;
    private Category category;
    private CategoryBudget categoryBudget;

    @BeforeEach
    void initialiseObjects() {
        currency = Currency.builder()
                .currencyId(1L)
                .currencyCode("INR")
                .symbol((char) 8377)
                .exchangeRate(BigDecimal.valueOf(99))
                .build();
        user = User.builder()
                .userID(1L)
                .username("Vedant")
                .email("vedant@gmail.com")
                .passwordHash("anything")
                .monthlyIncome(BigDecimal.valueOf(27800))
                .currency(currency)
                .role(Role.USER)
                .build();
        account = Account.builder()
                .accountId(1L)
                .accountNumber("12345678")
                .accountType(Type.CASH)
                .currency(currency)
                .name("Cash")
                .currentBalance(BigDecimal.valueOf(2030))
                .user(user)
                .build();
        category = Category.builder()
                .categoryId(1L)
                .categoryName("1st Category")
                .description("This is the first category")
                .user(user)
                .build();
        categoryBudget = CategoryBudget.builder()
                .categoryBudgetId(1L)
                .category(category)
                .name("Budget 1 for category 1")
                .maxAllotedBudget(BigDecimal.valueOf(230))
                .account(account)
                .build();
    }

    @Test
    void create_shouldCreateCategoryBudget() {
        String email = "vedant@gmail.com";
        CategoryBudgetCreationRequest request = CategoryBudgetCreationRequest.builder()
                .accountId(account.getAccountId())
                .categoryId(category.getCategoryId())
                .name(categoryBudget.getName())
                .maxAllotedBudget(categoryBudget.getMaxAllotedBudget())
                .build();
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(accountRepository.findByAccountIdAndUser(request.accountId(), user))
                .thenReturn(Optional.of(account));
        when(categoryRepository.findById(request.categoryId()))
                .thenReturn(Optional.of(category));
        when(categoryBudgetRepository.save(any(CategoryBudget.class)))
                .thenReturn(categoryBudget);
        CategoryBudgetCreationResponse response = categoryBudgetService.create(request, email);
        assertEquals(1L, response.categoryBudgetId());
        assertEquals("Budget 1 for category 1", response.name());
        assertEquals(new BigDecimal("230"), response.maxAllotedBudget());

        verify(userRepository).findByEmail(email);
        verify(accountRepository).findByAccountIdAndUser(request.accountId(), user);
        verify(categoryRepository).findById(request.categoryId());
        verify(categoryBudgetRepository).save(any(CategoryBudget.class));
    }

    @Test
    void create_shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {

        String email = "unknown@gmail.com";

        CategoryBudgetCreationRequest request = CategoryBudgetCreationRequest.builder()
                .accountId(account.getAccountId())
                .categoryId(category.getCategoryId())
                .name(categoryBudget.getName())
                .maxAllotedBudget(categoryBudget.getMaxAllotedBudget())
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> categoryBudgetService.create(request, email));

        verify(userRepository).findByEmail(email);

        verify(accountRepository, never())
                .findByAccountIdAndUser(any(), any());

        verify(categoryRepository, never())
                .findById(any());

        verify(categoryBudgetRepository, never())
                .save(any(CategoryBudget.class));
    }

    @Test
    void create_shouldThrowAccountNotFoundExceptionWhenAccountDoesNotExistForUser() {

        String email = "vedant@gmail.com";

        CategoryBudgetCreationRequest request = CategoryBudgetCreationRequest.builder()
                .accountId(999L)
                .categoryId(category.getCategoryId())
                .name(categoryBudget.getName())
                .maxAllotedBudget(categoryBudget.getMaxAllotedBudget())
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByAccountIdAndUser(request.accountId(), user))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> categoryBudgetService.create(request, email));

        verify(userRepository).findByEmail(email);

        verify(accountRepository)
                .findByAccountIdAndUser(request.accountId(), user);

        verify(categoryRepository, never())
                .findById(any());

        verify(categoryBudgetRepository, never())
                .save(any(CategoryBudget.class));
    }

    @Test
    void create_shouldThrowCategoryNotFoundExceptionWhenCategoryDoesNotExist() {

        String email = "vedant@gmail.com";

        CategoryBudgetCreationRequest request = CategoryBudgetCreationRequest.builder()
                .accountId(account.getAccountId())
                .categoryId(999L)
                .name(categoryBudget.getName())
                .maxAllotedBudget(categoryBudget.getMaxAllotedBudget())
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByAccountIdAndUser(request.accountId(), user))
                .thenReturn(Optional.of(account));

        when(categoryRepository.findById(request.categoryId()))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> categoryBudgetService.create(request, email));

        verify(userRepository).findByEmail(email);

        verify(accountRepository)
                .findByAccountIdAndUser(request.accountId(), user);

        verify(categoryRepository)
                .findById(request.categoryId());

        verify(categoryBudgetRepository, never())
                .save(any(CategoryBudget.class));
    }
}
