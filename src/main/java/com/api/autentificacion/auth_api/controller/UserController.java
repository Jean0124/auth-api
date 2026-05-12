package com.api.autentificacion.auth_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    // Ruta accesible para usuarios autenticados
    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> profile(
            Authentication authentication) {

        return ResponseEntity.ok(Map.of(
                "email", authentication.getName(),
                "mensaje", "Perfil obtenido correctamente"
                ));
    }

    // Ruta accesible solo para usuarios con rol ADMIN
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String,String>> adminDashboard() {
        return ResponseEntity.ok(Map.of(
                "mensaje", "Bienvenido al dashboard de administración"
        ));
    }

}
