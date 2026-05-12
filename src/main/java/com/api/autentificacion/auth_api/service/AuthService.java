package com.api.autentificacion.auth_api.service;

import com.api.autentificacion.auth_api.dto.AuthResponse;
import com.api.autentificacion.auth_api.dto.LoginRequest;
import com.api.autentificacion.auth_api.dto.RequestRegister;
import com.api.autentificacion.auth_api.model.Rol;
import com.api.autentificacion.auth_api.model.Usuario;
import com.api.autentificacion.auth_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoderService passwordEncoderService;

    public AuthResponse registrar (RequestRegister requestRegister){

        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(requestRegister.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Crear el usuario
        Usuario usuario = Usuario.builder()
                .name(requestRegister.getName())
                .email(requestRegister.getEmail())
                .password(passwordEncoderService.encode(requestRegister.getPassword()))
                .rol(Rol.USUARIO) // Asignar un rol por defecto
                .build();

        usuarioRepository.save(usuario);

        // Generar el token JWT y retornar la respuesta
        String token = jwtService.gnerateToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .email(usuario.getEmail())
                .role(usuario.getRol().name())
                .build();

    }

    public AuthResponse login (LoginRequest loginRequest){

        //Buscar el usuario por email
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Verificar la contraseña
        if (!passwordEncoderService.matches(loginRequest.getPassword(), usuario.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        // Generar el token JWT y retornar la respuesta
        String token = jwtService.gnerateToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .email(usuario.getEmail())
                .role(usuario.getRol().name())
                .build();
    }


}
