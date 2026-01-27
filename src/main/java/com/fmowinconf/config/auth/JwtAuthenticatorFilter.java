package com.fmowinconf.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JwtAuthenticatorFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticatorFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = recuperarTokenDeCookie(request);

            if (token != null) {
                ContenidoJWT contenido = jwtService.extraerContenido(token);

                if (contenido != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Validación extra: verificar si el token expiró (si no lo hace ya
                    // extraerContenido)
                    if (jwtService.isTokenExpired(token)) {
                        throw new JwtException("El token ha expirado");
                    }

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            contenido, null, new ArrayList<>());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException e) {
            // Captura errores específicos de la librería JWT o de lógica de seguridad
            SecurityContextHolder.clearContext();
            enviarErrorJson(response, "Token inválido o expirado: " + e.getMessage());
        } catch (Exception e) {
            // Error genérico del sistema
            SecurityContextHolder.clearContext();
            enviarErrorJson(response, "Error procesando la autenticación");
        }
    }

    // Método auxiliar para buscar la cookie
    private String recuperarTokenDeCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> "jwt-token".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    // Método para escribir la respuesta de error manualmente
    private void enviarErrorJson(HttpServletResponse response, String mensaje) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\"}", mensaje);
        response.getWriter().write(json);
    }
}