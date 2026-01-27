package com.fmowinconf.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.dto.request.EditarFMO;
import com.fmowinconf.dto.request.EliminarObjetos;
import com.fmowinconf.dto.response.ConfiguracionDTO;
import com.fmowinconf.models.Configuracion;
import com.fmowinconf.services.ConfiguracionServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ConfiguracionController {
    @Autowired
    private ConfiguracionServiceImpl configuracionService;

    @PostMapping("configuraciones/crearConfiguracion")
    public ResponseEntity<?> crearConfiguracion(@RequestBody ConfiguracionDTO entity) {
        try {
            Configuracion configuracion = configuracionService.crearConfiguracion(entity);
            return ResponseEntity.ok(configuracion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la configuración: " + e.getMessage());
        }
    }

    @GetMapping("configuraciones")
    public ResponseEntity<?> getMethodName(
            @RequestParam(value = "desde", required = false) String desde,
            @RequestParam(value = "hasta", required = false) String hasta,
            @RequestParam(value = "ficha", required = false) String ficha,
            @RequestParam(value = "sistema_operativo", required = false) String sistema_operativo,
            @RequestParam(value = "fmo_equipo", required = false) String fmo_equipo,
            @RequestParam(value = "nombre_completo", required = false) String nombre_completo,
            @RequestParam(value = "exacto", required = false) Boolean exacto) {

        // 1. Prioridad: Búsqueda por analista
        if (ficha != null) {
            if (exacto != null && exacto) {
                return ResponseEntity.ok()
                        .body(configuracionService.obtenerConfiguracionesPorFichaAnalistaExacta(ficha));
            }
            return ResponseEntity.ok().body(configuracionService.obtenerPorFichaAnalista(ficha));
        }

        if (sistema_operativo != null) {
            return ResponseEntity.ok().body(configuracionService.obtenerPorSistemaOperativo(sistema_operativo));
        }

        if (fmo_equipo != null) {
            return ResponseEntity.ok().body(configuracionService.obtenerPorFmoEquipo(fmo_equipo));
        }

        if (nombre_completo != null) {
            return ResponseEntity.ok().body(configuracionService.obtenerPorNombreAnalista(nombre_completo));
        }

        // 2. CASO 0: Si ambos parámetros son nulos o vacíos, devolvemos todo
        // Usamos paréntesis para agrupar correctamente la lógica
        boolean desdeEsNuloOVacio = (desde == null || desde.trim().isEmpty());
        boolean hastaEsNuloOVacio = (hasta == null || hasta.trim().isEmpty());

        if (desdeEsNuloOVacio && hastaEsNuloOVacio) {
            return ResponseEntity.ok().body(configuracionService.obtenerTodasLasConfiguraciones());
        }

        // 3. CASO 1: Si no envía 'desde', fecha mínima
        if (desdeEsNuloOVacio) {
            desde = "1900-01-01 00:00:00";
        }

        // 4. CASO 2: Si no envía 'hasta', fecha actual (AHORA)
        if (hastaEsNuloOVacio) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss");
            hasta = java.time.LocalDateTime.now().format(formatter);
        }

        return ResponseEntity.ok().body(configuracionService.obtenerConfiguracionEntreFechas(desde, hasta));
    }

    @PostMapping("configuraciones/delete")
    public ResponseEntity<?> postMethodName(@RequestBody EliminarObjetos objetoAEliminar) {
        try {
            configuracionService.ocultarConfiguracion(objetoAEliminar.getId());
            System.out.println("Eliminar configuración con ID: " + objetoAEliminar.getId());
            return ResponseEntity.ok(Map.of("message", "Configuración eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar la configuración: " + e.getMessage());
        }
    }

    @PostMapping("/configuraciones/editar-fmo")
    public ResponseEntity<?> editarFMO(@RequestBody EditarFMO editarFMO) {
        try {
            configuracionService.editarFmoEquipoConfiguracion(editarFMO.getId(), editarFMO.getFmo_equipo());
            return ResponseEntity.ok(Map.of("ok", true));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error al editar el FMO: " + ex.getMessage());
        }
    }
}
