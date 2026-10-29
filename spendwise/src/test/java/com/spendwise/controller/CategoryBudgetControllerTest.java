package com.spendwise.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.spendwise.dto.CategoryBudgetCreationRequest;
import com.spendwise.dto.CategoryBudgetCreationResponse;
import com.spendwise.service.CategoryBudgetService;

@ExtendWith(MockitoExtension.class)
class CategoryBudgetControllerTest {

    private MockMvc mockMvc;

    private final String email = "vedant@gmail.com";

    private UsernamePasswordAuthenticationToken authentication;

    @Mock
    private CategoryBudgetService categoryBudgetService;

    @InjectMocks
    private CategoryBudgetController categoryBudgetController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryBudgetController)
                .build();

        authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null
                );
    }

    @Test
    void createBudget_shouldReturnCreatedAndCreatedBudget()
            throws Exception {

        // Arrange
        CategoryBudgetCreationResponse serviceResponse =
                CategoryBudgetCreationResponse.builder()
                        .categoryBudgetId(1L)
                        .name("Monthly Food Budget")
                        .maxAllotedBudget(new BigDecimal("10000"))
                        .build();

        when(categoryBudgetService.create(
                any(CategoryBudgetCreationRequest.class),
                eq(email)
        )).thenReturn(serviceResponse);

        String requestBody = """
                {
                    "accountId": 10,
                    "categoryId": 5,
                    "name": "Monthly Food Budget",
                    "maxAllotedBudget": 10000
                }
                """;

        // Act + Assert
        mockMvc.perform(
                        post("/budget")
                                .principal(authentication)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryBudgetId")
                        .value(1))
                .andExpect(jsonPath("$.name")
                        .value("Monthly Food Budget"))
                .andExpect(jsonPath("$.maxAllotedBudget")
                        .value(10000));

        // Verify request deserialization and authentication forwarding
        ArgumentCaptor<CategoryBudgetCreationRequest> requestCaptor =
                ArgumentCaptor.forClass(
                        CategoryBudgetCreationRequest.class
                );

        verify(categoryBudgetService).create(
                requestCaptor.capture(),
                eq(email)
        );

        CategoryBudgetCreationRequest capturedRequest =
                requestCaptor.getValue();

        assertEquals(
                10L,
                capturedRequest.accountId()
        );

        assertEquals(
                5L,
                capturedRequest.categoryId()
        );

        assertEquals(
                "Monthly Food Budget",
                capturedRequest.name()
        );

        assertEquals(
                0,
                new BigDecimal("10000")
                        .compareTo(
                                capturedRequest.maxAllotedBudget()
                        )
        );
    }
}