package com.api.autentificacion.auth_api.security;

import com.api.autentificacion.auth_api.model.Usuario;
import com.api.autentificacion.auth_api.repository.UsuarioRepository;
import com.api.autentificacion.auth_api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter  extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException{

        // 1 Obtener el header de autorización
        String authHeader = request.getHeader("Authorization");

        // 2 si no tiene token, continuar con la cadena de filtros
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3 Extraer el token
        String token = authHeader.substring(7);

        // 4 extrear el email del token
        String email = jwtService.extractEmail(token);

        // 5 si hay email y no hay autenticación en el contexto de seguridad
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // 6 Buscar el usuario en la base de datos
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 7 Validar el token y cargar la autenticación en el contexto de seguridad
            if (!jwtService.isExpired(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities() // ✅ trae los roles correctamente
                        );
               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

               SecurityContextHolder.getContext().setAuthentication(authToken);
           }

        }

        filterChain.doFilter(request, response);
    }

}
