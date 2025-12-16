package com.fmowinconf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.dto.response.AnalistaDTO;
import com.fmowinconf.models.Analista;
import com.fmowinconf.repository.AnalistaRepository;
import com.fmowinconf.services.IAnalistaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AnalistaController {

    @Autowired
    private IAnalistaService analistaService;

    @GetMapping("/analistas")
    public ResponseEntity<?> getAllAnalistas() {

        return ResponseEntity.ok(analistaService.getAllAnalistas());
    }

    @PostMapping("/analistas/crearAnalista")
    public ResponseEntity<?> saveAnalista(@RequestBody AnalistaDTO analista) {
        try {
            analistaService.crearAnalista(analista);
            return ResponseEntity.ok().body(analista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el analista: " + e.getMessage());
        }
    }

}
