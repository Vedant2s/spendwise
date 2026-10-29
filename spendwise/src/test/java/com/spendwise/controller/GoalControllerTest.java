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
import java.time.LocalDate;

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

import com.spendwise.dto.GoalCreationRequest;
import com.spendwise.dto.GoalCreationResponse;
import com.spendwise.service.GoalService;

@ExtendWith(MockitoExtension.class)
class GoalControllerTest {

    private MockMvc mockMvc;

    private final String email = "vedant@gmail.com";

    private UsernamePasswordAuthenticationToken authentication;

    @Mock
    private GoalService goalService;

    @InjectMocks
    private GoalController goalController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(goalController)
                .build();

        authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null
                );
    }

    @Test
    void createGoal_shouldReturnCreatedAndCreatedGoal()
            throws Exception {

        // Arrange
        GoalCreationResponse serviceResponse =
                GoalCreationResponse.builder()
                        .goalId(1L)
                        .name("Buy New Bike")
                        .targetAmount(new BigDecimal("200000"))
                        .currentAmount(new BigDecimal("50000"))
                        .desiredDate(LocalDate.of(2027, 12, 31))
                        .build();

        when(goalService.create(
                any(GoalCreationRequest.class),
                eq(email)
        )).thenReturn(serviceResponse);

        /*
         * Important:
         * The JSON property is desired_date because that is the
         * exact component name in GoalCreationRequest.
         */
        String requestBody = """
                {
                    "name": "Buy New Bike",
                    "targetAmount": 200000,
                    "currentAmount": 50000,
                    "desired_date": "2027-12-31"
                }
                """;

        // Act + Assert
        mockMvc.perform(
                        post("/goals")
                                .principal(authentication)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.goalId")
                        .value(1))
                .andExpect(jsonPath("$.name")
                        .value("Buy New Bike"))
                .andExpect(jsonPath("$.targetAmount")
                        .value(200000))
                .andExpect(jsonPath("$.currentAmount")
                        .value(50000))
                .andExpect(jsonPath("$.desiredDate")
                        .value("2027-12-31"));

        /*
         * Capture the DTO sent to GoalService to verify
         * request deserialization.
         */
        ArgumentCaptor<GoalCreationRequest> requestCaptor =
                ArgumentCaptor.forClass(
                        GoalCreationRequest.class
                );

        verify(goalService).create(
                requestCaptor.capture(),
                eq(email)
        );

        GoalCreationRequest capturedRequest =
                requestCaptor.getValue();

        assertEquals(
                "Buy New Bike",
                capturedRequest.name()
        );

        assertEquals(
                0,
                new BigDecimal("200000")
                        .compareTo(capturedRequest.targetAmount())
        );

        assertEquals(
                0,
                new BigDecimal("50000")
                        .compareTo(capturedRequest.currentAmount())
        );

        assertEquals(
                LocalDate.of(2027, 12, 31),
                capturedRequest.desired_date()
        );
    }
}