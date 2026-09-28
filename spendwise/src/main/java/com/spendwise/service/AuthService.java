package com.spendwise.service;


import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spendwise.config.Role;
import com.spendwise.dto.LoginRequest;
// import com.spendwise.dto.LoginResponse;
import com.spendwise.dto.RegisterRequest;
import com.spendwise.dto.AuthenticationResponse;
import com.spendwise.exception.customException.InvalidPasswordException;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.exception.customException.UserWithEmailAlreadyExists;
import com.spendwise.model.Currency;
import com.spendwise.model.User;
import com.spendwise.repository.CurrencyRepository;
import com.spendwise.repository.UserRepository;
import com.spendwise.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CurrencyRepository currRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public boolean isVerified(User user) {
        // passwordHash matching goes here
        return true;
    }

    public AuthenticationResponse login(LoginRequest request) {
        // User user = userRepository.findByEmail(request.email())
        //         .orElseThrow(UserNotFoundException::new);
        // if (!isVerified(user)) {
        //     throw new InvalidPasswordException();
        // }
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.passwordHash())
        );
        var user=userRepository.findByEmail(request.email())
            .orElseThrow(()->new UsernameNotFoundException("Username is not found"));
        var jwtToken=jwtService.generateToken(new HashMap<>(),user);
        return AuthenticationResponse.builder()
            .isRegistered(true)
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserWithEmailAlreadyExists();
        }
        System.out.println(currRepository.findAll());
        var newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(request.passwordHash())
                .monthlyIncome(request.monthlyIncome())
                .currency(currRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default currency not found")))
                .role(Role.USER)
                .build();
        userRepository.save(newUser);
        var jwtToken=jwtService.generateToken(new HashMap<>(),newUser);
        return AuthenticationResponse.builder()
            .isRegistered(true)
            .token(jwtToken)
            .build();
    }

}
