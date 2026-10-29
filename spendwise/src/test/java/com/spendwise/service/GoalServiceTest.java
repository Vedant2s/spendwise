package com.spendwise.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spendwise.dto.GoalCreationRequest;
import com.spendwise.dto.GoalCreationResponse;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.model.Goal;
import com.spendwise.model.User;
import com.spendwise.repository.GoalRepository;
import com.spendwise.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GoalService goalService;

    @Test
    void create_shouldCreateGoalSuccessfully() {

        String email = "test@gmail.com";

        User user = User.builder()
                .email(email)
                .build();

        GoalCreationRequest request = GoalCreationRequest.builder()
                .name("Buy Bike")
                .currentAmount(new BigDecimal("20000"))
                .targetAmount(new BigDecimal("100000"))
                .desired_date(LocalDate.of(2027, 1, 1))
                .build();

        Goal savedGoal = Goal.builder()
                .goalId(1L)
                .name("Buy Bike")
                .currentAmount(new BigDecimal("20000"))
                .targetAmount(new BigDecimal("100000"))
                .desiredDate(LocalDate.of(2027, 1, 1))
                .user(user)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(goalRepository.save(any(Goal.class)))
                .thenReturn(savedGoal);

        GoalCreationResponse response = goalService.create(request, email);

        assertNotNull(response);

        assertEquals(1L, response.goalId());
        assertEquals("Buy Bike", response.name());
        assertEquals(
                new BigDecimal("20000"),
                response.currentAmount()
        );
        assertEquals(
                new BigDecimal("100000"),
                response.targetAmount()
        );
        assertEquals(
                LocalDate.of(2027, 1, 1),
                response.desiredDate()
        );

        verify(userRepository).findByEmail(email);
        verify(goalRepository).save(any(Goal.class));
    }


    @Test
    void create_shouldThrowUserNotFoundException_whenUserDoesNotExist() {

        String email = "missing@gmail.com";

        GoalCreationRequest request = GoalCreationRequest.builder()
                .name("Buy Bike")
                .currentAmount(new BigDecimal("20000"))
                .targetAmount(new BigDecimal("100000"))
                .desired_date(LocalDate.of(2027, 1, 1))
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> goalService.create(request, email)
        );

        verify(userRepository).findByEmail(email);

        verify(goalRepository, never())
                .save(any(Goal.class));
    }
}