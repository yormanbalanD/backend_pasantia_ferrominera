package com.fmowinconf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.dto.request.EditarAnalistaDTO;
import com.fmowinconf.dto.response.CrearAnalistaDTO;
import com.fmowinconf.models.Analista;
import com.fmowinconf.services.IAnalistaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AnalistaController {

    @Autowired
    private IAnalistaService analistaService;

    @PostMapping("/analista/delete")
    public ResponseEntity<?> postMethodName(@RequestBody long id) {
        try {
            return ResponseEntity.ok(analistaService.ocultarAnalista(id));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el analista: " + e.getMessage());
        }
    }
    

    @GetMapping("/analistas")
    public ResponseEntity<?> getAllAnalistas() {

        return ResponseEntity.ok(analistaService.getAllAnalistas());
    }

    @PostMapping("/analistas/crearAnalista")
    public ResponseEntity<?> saveAnalista(@RequestBody CrearAnalistaDTO analista) {
        try {
            Analista analistaCreado = analistaService.crearAnalista(analista);
            return ResponseEntity.ok().body(analistaCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el analista: " + e.getMessage());
        }
    }

    @PostMapping("/analistas/editarAnalista/{id}")
    public ResponseEntity<?> editarAnalista(@PathVariable Long id, @RequestBody EditarAnalistaDTO analistaDTO) {
        try {
            Analista analistaEditado = analistaService.editarAnalista(id, analistaDTO);
            return ResponseEntity.ok().body(analistaEditado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al editar el analista: " + e.getMessage());
        }
    }

}
