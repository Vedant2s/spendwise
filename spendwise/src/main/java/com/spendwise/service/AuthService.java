package com.spendwise.service;

import com.spendwise.dto.LoginRequest;
import com.spendwise.dto.LoginResponse;
import com.spendwise.dto.RegisterRequest;
import com.spendwise.dto.RegisterResponse;
import com.spendwise.exception.customException.InvalidPasswordException;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.exception.customException.UserWithEmailAlreadyExists;
import com.spendwise.model.User;
import com.spendwise.repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new UserWithEmailAlreadyExists();
        }
        User newUser = new User(
            request.username(),
            request.email(),
            request.passwordHash(),
            request.monthlyIncome());
        userRepository.save(newUser);
        return new RegisterResponse(true,"User registered");
    }

}
