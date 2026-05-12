package com.api.autentificacion.auth_api.service;

import com.api.autentificacion.auth_api.model.Rol;
import com.api.autentificacion.auth_api.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;
    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {

        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", "mi_clave_super_secreta_para_test_minimo_32_caracteres");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);

        usuarioTest = Usuario.builder()
                .email("jean@test.com")
                .password("password123")
                .rol(Rol.USUARIO)
                .build();
    }

    @Test
    void debeGenerarTokenNoNulo() {
        String token = jwtService.gnerateToken(usuarioTest);
        assertNotNull(token);
    }

    @Test
    void debeExtraerEmailCorrectamente() {
        String token = jwtService.gnerateToken(usuarioTest);
        String email = jwtService.extractEmail(token);
        assertEquals("jean@test.com", email);
    }

    @Test
    void tokenDebeSerValido() {
        String token = jwtService.gnerateToken(usuarioTest);
        assertTrue(jwtService.isValid(token, usuarioTest));
    }

    @Test
    void tokenNoDebeEstarExpirado() {
        String token = jwtService.gnerateToken(usuarioTest);
        assertFalse(jwtService.isExpired(token));
    }
}
