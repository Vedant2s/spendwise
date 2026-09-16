package com.spendwise.service;


import com.spendwise.dto.LoginRequest;
import com.spendwise.dto.LoginResponse;
import com.spendwise.exception.customException.InvalidPasswordException;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.model.User;
import com.spendwise.repository.UserRepository;

public class LogInService {

    private final UserRepository userRepository;
    public LogInService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public boolean isVerified(User user) {
        //passwordHash matching goes here
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

}
