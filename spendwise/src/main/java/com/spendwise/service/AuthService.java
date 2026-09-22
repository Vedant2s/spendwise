package com.spendwise.service;


import org.springframework.stereotype.Service;

import com.spendwise.dto.LoginRequest;
import com.spendwise.dto.LoginResponse;
import com.spendwise.dto.RegisterRequest;
import com.spendwise.dto.RegisterResponse;
import com.spendwise.exception.customException.InvalidPasswordException;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.exception.customException.UserWithEmailAlreadyExists;
import com.spendwise.model.Currency;
import com.spendwise.model.User;
import com.spendwise.repository.CurrencyRepository;
import com.spendwise.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CurrencyRepository currRepository;

    public AuthService(UserRepository userRepository, CurrencyRepository currRepository) {
        this.userRepository = userRepository;
        this.currRepository = currRepository;
    }

    public boolean isVerified(User user) {
        // passwordHash matching goes here
        return true;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);
        if (!isVerified(user)) {
            throw new InvalidPasswordException();
        }

        return new LoginResponse(true, "Log in successfull");
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserWithEmailAlreadyExists();
        }
        User newUser = new User(
                request.username(),
                request.email(),
                request.passwordHash(),
                request.monthlyIncome());
        Currency defaultCurr = currRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default currency not found"));
        newUser.setCurrency(defaultCurr);
        userRepository.save(newUser);
        return new RegisterResponse(true, "User registered");
    }

}
