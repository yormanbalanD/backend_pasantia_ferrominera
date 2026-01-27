package com.fmowinconf.controllers;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.dto.request.LoginRequest;
import com.fmowinconf.dto.response.LoginDTO;
import com.fmowinconf.services.AuthService;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verificamos:
        // 1. Que no sea nulo
        // 2. Que esté autenticado
        // 3. Que NO sea un usuario anónimo (Spring pone uno por defecto si no hay
        // token)
        if (auth == null || !auth.isAuthenticated() ||
                auth instanceof AnonymousAuthenticationToken) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "authenticated", false,
                    "message", "No hay una sesión activa"));
        }
        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "analista", auth.getPrincipal()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginDTO data = authService.autenticar(request);

            ResponseCookie cookie = ResponseCookie.from("jwt-token", data.getToken())
                    .httpOnly(true) // Protege contra XSS
                    // .secure(true) // Solo se envía por HTTPS
                    .path("/") // Disponible en toda la app
                    .maxAge(3600 * 4) // Expiración en segundos (4 horas)
                    .sameSite("Strict") // Previene ataques CSRF
                    .build();

            // 3. Agregar la cookie al header de la respuesta
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of(
                            "authenticated", true,
                            "ficha", data.getFicha(),
                            "nombre_completo", data.getNombre_completo(),
                            "id", data.getId(),
                            "permisos", data.getPermisos()));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Credenciales inválidas")) {
                return ResponseEntity.status(401).body(new LoginDTO(false, null, "Credenciales inválidas"));
            }

            return ResponseEntity.status(400).body(new LoginDTO(false, null, e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Creamos una cookie "vacía"
        ResponseCookie cookie = ResponseCookie.from("jwt-token", "")
                .httpOnly(true)
                .secure(false) // Ponlo en true si usas HTTPS (Producción)
                .path("/")
                .maxAge(0) // Duración de 0 segundos = Borrado inmediato
                .sameSite("Strict")
                .build();

        // Limpiamos el contexto de seguridad de Spring de forma manual por seguridad
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Sesión cerrada exitosamente");
    }

}