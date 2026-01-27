package com.fmowinconf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmowinconf.config.auth.ContenidoJWT;
import com.fmowinconf.config.auth.JwtService;
import com.fmowinconf.dto.request.LoginRequest;
import com.fmowinconf.dto.response.LoginDTO;
import com.fmowinconf.models.Analista;
import com.fmowinconf.repository.AnalistaRepository;

import java.util.Date;

@Service
public class AuthService {

    private final JwtService jwtService;
    // Aquí inyectarías tu UserRepository y el PasswordEncoder

    @Autowired
    private AnalistaRepository analistaRepository;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public LoginDTO autenticar(LoginRequest request) {
        // 1. Validar las credenciales (Ejemplo simplificado)
        Analista analista = analistaRepository.findByFicha(request.getFicha());

        if (analista == null || analista.getPassword().compareTo(request.getPassword()) != 0) {
        // if (analista == null || !passwordEncoder.matches(request.getPassword(), analista.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 2. Crear la instancia de tu clase personalizada ContenidoJWT
        ContenidoJWT contenido = new ContenidoJWT();
        contenido.setId(analista.getId()); // Ej: usuario.getId().toString()
        contenido.setNombre_completo(analista.getNombre_completo()); // Ej: usuario.getNombre()
        contenido.setFicha(analista.getFicha()); // Ej: usuario.getFicha()
        contenido.setPermisos(analista.getPermisos()); // Ej: usuario.getPermisos()

        LoginDTO loginDTO = new LoginDTO();
        String token = jwtService.generarToken(contenido);
        loginDTO.setToken(token);
        loginDTO.setOk(true);
        loginDTO.setFicha(analista.getFicha());
        loginDTO.setNombre_completo(analista.getNombre_completo());
        loginDTO.setPermisos(analista.getPermisos());
        loginDTO.setId(analista.getId());

        return loginDTO;
    }
}