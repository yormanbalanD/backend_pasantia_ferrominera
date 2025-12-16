package com.fmowinconf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.dto.response.ConfiguracionDTO;
import com.fmowinconf.models.Configuracion;
import com.fmowinconf.services.ConfiguracionServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class ConfiguracionController {
    @Autowired
    private ConfiguracionServiceImpl configuracionService;

    @PostMapping("configuraciones/crearConfiguracion")
    public ResponseEntity<?> crearConfiguracion(@RequestBody ConfiguracionDTO entity) {
        try {
            Configuracion configuracion = configuracionService.crearConfiguracion(entity);
            return ResponseEntity.ok().body(configuracion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la configuración: " + e.getMessage());
        }
    }   
}
