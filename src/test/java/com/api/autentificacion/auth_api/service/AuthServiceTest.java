package com.api.autentificacion.auth_api.service;

import com.api.autentificacion.auth_api.dto.AuthResponse;
import com.api.autentificacion.auth_api.dto.LoginRequest;
import com.api.autentificacion.auth_api.dto.RequestRegister;
import com.api.autentificacion.auth_api.model.Rol;
import com.api.autentificacion.auth_api.model.Usuario;
import com.api.autentificacion.auth_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {


    @Mock
    private UsuarioRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoderService passwordEncoderService;

    private AuthService authService;

    private Usuario testUser;
    private RequestRegister registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {

        authService = new AuthService(userRepository, jwtService, passwordEncoderService);

        testUser = Usuario.builder()
                .email("jean@test.com")
                .password("passwordEncriptado")
                .rol(Rol.USUARIO)
                .name("Jean Pierre")
                .build();

        registerRequest = new RequestRegister();
        registerRequest.setName("Jean Pierre");
        registerRequest.setEmail("jean@test.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("jean@test.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void debeRegistrarUsuarioCorrectamente() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoderService.encode(any())).thenReturn("passwordEncriptado");
        when(userRepository.save(any())).thenReturn(testUser);
        when(jwtService.gnerateToken(any())).thenReturn("token.jwt.test");

        AuthResponse response = authService.registrar(registerRequest);

        assertNotNull(response);
        assertEquals("token.jwt.test", response.getToken());
        assertEquals("jean@test.com", response.getEmail());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void debeLanzarExcepcionSiEmailYaExiste() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> authService.registrar(registerRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    void debeHacerLoginCorrectamente() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));
        when(passwordEncoderService.matches(any(), any())).thenReturn(true);
        when(jwtService.gnerateToken(any())).thenReturn("token.jwt.test");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("token.jwt.test", response.getToken());
        assertEquals("jean@test.com", response.getEmail());
    }

    @Test
    void debeLanzarExcepcionSiUsuarioNoExiste() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));
    }

    @Test
    void debeLanzarExcepcionSiPasswordIncorrecta() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));
        when(passwordEncoderService.matches(any(), any())).thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login(loginRequest));
    }
}
