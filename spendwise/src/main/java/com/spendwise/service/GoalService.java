package com.spendwise.service;

import org.springframework.stereotype.Service;

import com.spendwise.dto.GoalCreationRequest;
import com.spendwise.dto.GoalCreationResponse;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.model.Goal;
import com.spendwise.repository.GoalRepository;
import com.spendwise.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    public GoalCreationResponse create(GoalCreationRequest request, String email) {
        var user=userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException());
        var newGoal = Goal.builder()
                .currentAmount(request.currentAmount())
                .desiredDate(request.desired_date())
                .name(request.name())
                .targetAmount(request.targetAmount())
                .user(user)
                .build();
        var savedGoal=goalRepository.save(newGoal);
        return GoalCreationResponse.builder()
                .goalId(savedGoal.getGoalId())
                .currentAmount(savedGoal.getCurrentAmount())
                .desiredDate(savedGoal.getDesiredDate())
                .name(savedGoal.getName())
                .targetAmount(savedGoal.getTargetAmount())
                .build();
    }

}
