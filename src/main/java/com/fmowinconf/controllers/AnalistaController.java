package com.fmowinconf.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.config.auth.ContenidoJWT;
import com.fmowinconf.dto.request.EditarAnalistaDTO;
import com.fmowinconf.dto.request.EliminarObjetos;
import com.fmowinconf.dto.response.CrearAnalistaDTO;
import com.fmowinconf.models.Analista;
import com.fmowinconf.services.IAnalistaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AnalistaController {

    @Autowired
    private IAnalistaService analistaService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/analista/delete")
    public ResponseEntity<?> postMethodName(@RequestBody long id) {
        try {
            return ResponseEntity.ok(analistaService.ocultarAnalista(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el analista: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/analistas")
    public ResponseEntity<?> getAllAnalistas(Authentication authentication) {
        // Obtenemos el principal que seteamos en el filtro
        ContenidoJWT usuario = (ContenidoJWT) authentication.getPrincipal();

        // Obtenemos todos los analistas del servicio
        List<Analista> listaCompleta = analistaService.getAllAnalistas();

        // Si el usuario es ANALISTA, filtramos los que tengan visible == 0
        if ("ANALISTA".equals(usuario.getPermisos())) {
            List<Analista> listaFiltrada = listaCompleta.stream()
                    .filter(a -> a.getVisible() != 0)
                    .toList();
            return ResponseEntity.ok(listaFiltrada);
        }

        // Si es ADMIN (o cualquier otro no restringido), enviamos todo
        return ResponseEntity.ok(listaCompleta);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/analistas/crearAnalista")
    public ResponseEntity<?> saveAnalista(@RequestBody CrearAnalistaDTO analista) {
        try {
            Analista analistaCreado = analistaService.crearAnalista(analista);
            return ResponseEntity.ok().body(analistaCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el analista: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/analistas/editarAnalista/{id}")
    public ResponseEntity<?> editarAnalista(@PathVariable Long id, @RequestBody EditarAnalistaDTO analistaDTO) {
        try {
            Analista analistaEditado = analistaService.editarAnalista(id, analistaDTO);
            return ResponseEntity.ok().body(analistaEditado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/analistas/delete")
    public ResponseEntity<?> postMethodName(@RequestBody EliminarObjetos objeto) {
        try {
            analistaService.eliminarAnalista(objeto.getId());
            return ResponseEntity.ok().body("Analista eliminado con exito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
