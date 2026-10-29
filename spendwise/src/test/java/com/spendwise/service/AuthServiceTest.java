package com.spendwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spendwise.config.Role;
import com.spendwise.dto.AuthenticationResponse;
import com.spendwise.dto.LoginRequest;
import com.spendwise.dto.RegisterRequest;
import com.spendwise.exception.customException.UserWithEmailAlreadyExists;
import com.spendwise.model.Currency;
import com.spendwise.model.User;
import com.spendwise.repository.CurrencyRepository;
import com.spendwise.repository.UserRepository;
import com.spendwise.security.jwt.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyRepository currRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldReturnAuthenticationResponse_whenCredentialsAreValid() {

        // Arrange
        String email = "vedant@gmail.com";
        String password = "password123";
        String token = "generated-jwt-token";

        LoginRequest request = LoginRequest.builder()
                .email(email)
                .passwordHash(password)
                .build();

        User user = User.builder()
                .username("Vedant")
                .email(email)
                .passwordHash(password)
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenReturn(token);

        // Act
        AuthenticationResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals(true, response.isRegistered());
        assertEquals(token, response.token());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        verify(userRepository).findByEmail(email);
        verify(jwtService).generateToken(anyMap(), any(User.class));
    }

    @Test
    void login_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {

        // Arrange
        String email = "missing@gmail.com";
        String password = "password123";

        LoginRequest request = LoginRequest.builder()
                .email(email)
                .passwordHash(password)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // Act + Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> authService.login(request)
        );

        assertEquals("Username is not found", exception.getMessage());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        verify(userRepository).findByEmail(email);
        verify(jwtService, never())
                .generateToken(anyMap(), any(User.class));
    }

    @Test
    void register_shouldCreateUserAndReturnToken_whenEmailDoesNotExist() {

        // Arrange
        String email = "vedant@gmail.com";
        String token = "generated-jwt-token";

        RegisterRequest request = RegisterRequest.builder()
                .username("Vedant")
                .email(email)
                .passwordHash("password123")
                .monthlyIncome(new BigDecimal("50000"))
                .build();

        Currency defaultCurrency = Currency.builder()
                .currencyId(1L)
                .currencyCode("INR")
                .symbol('₹')
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        when(currRepository.findById(1L))
                .thenReturn(Optional.of(defaultCurrency));

        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenReturn(token);

        // Act
        AuthenticationResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals(true, response.isRegistered());
        assertEquals(token, response.token());

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("vedant@gmail.com", savedUser.getUsername());
        assertEquals(email, savedUser.getEmail());
        assertEquals("password123", savedUser.getPasswordHash());
        assertEquals(
                new BigDecimal("50000"),
                savedUser.getMonthlyIncome()
        );
        assertEquals(defaultCurrency, savedUser.getCurrency());
        assertEquals(Role.USER, savedUser.getRole());

        verify(userRepository).findByEmail(email);
        verify(currRepository).findById(1L);
        verify(jwtService).generateToken(anyMap(), any(User.class));
    }

    @Test
    void register_shouldThrowUserWithEmailAlreadyExists_whenEmailExists() {

        // Arrange
        String email = "existing@gmail.com";

        RegisterRequest request = RegisterRequest.builder()
                .username("Vedant")
                .email(email)
                .passwordHash("password123")
                .monthlyIncome(new BigDecimal("50000"))
                .build();

        User existingUser = User.builder()
                .username("Existing User")
                .email(email)
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(existingUser));

        // Act + Assert
        assertThrows(
                UserWithEmailAlreadyExists.class,
                () -> authService.register(request)
        );

        verify(userRepository).findByEmail(email);
        verify(currRepository, never()).findById(any());
        verify(userRepository, never()).save(any(User.class));
        verify(jwtService, never())
                .generateToken(anyMap(), any(User.class));
    }

    @Test
    void register_shouldThrowRuntimeException_whenDefaultCurrencyDoesNotExist() {

        // Arrange
        String email = "vedant@gmail.com";

        RegisterRequest request = RegisterRequest.builder()
                .username("Vedant")
                .email(email)
                .passwordHash("password123")
                .monthlyIncome(new BigDecimal("50000"))
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        when(currRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );

        assertEquals(
                "Default currency not found",
                exception.getMessage()
        );

        verify(userRepository).findByEmail(email);
        verify(currRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
        verify(jwtService, never())
                .generateToken(anyMap(), any(User.class));
    }
}