package com.fmowinconf.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.dto.request.EditarFMO;
import com.fmowinconf.dto.request.EliminarObjetos;
import com.fmowinconf.dto.response.RespaldoDTO;
import com.fmowinconf.models.Respaldo;
import com.fmowinconf.services.RespaldoServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class RespaldoController {

    @Autowired
    private RespaldoServiceImpl respaldoService;

    @PostMapping("/respaldos/crearRespaldo")
    public ResponseEntity<?> crearRespaldo(@RequestBody RespaldoDTO respaldoDTO) {
        try {
            return ResponseEntity.ok(respaldoService.crearRespaldo(respaldoDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el respaldo: " + e.getMessage());
        }
    }

    @GetMapping("/respaldos/analista/{id}")
    public ResponseEntity<List<Respaldo>> obtenerPorAnalista(@PathVariable("id") Long id) {
        List<Respaldo> lista = respaldoService.obtenerRespaldosPorAnalista(id);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/respaldos")
    public ResponseEntity<?> obtenerTodosLosRespaldos(
            @RequestParam(value = "desde", required = false) String desde,
            @RequestParam(value = "hasta", required = false) String hasta,
            @RequestParam(value = "ficha", required = false) String ficha,
            @RequestParam(value = "sistema_operativo", required = false) String sistema_operativo,
            @RequestParam(value = "fmo_equipo", required = false) String fmo_equipo,
            @RequestParam(value = "nombre_completo", required = false) String nombre_completo,
            @RequestParam(value = "exacto", required = false) Boolean exacto) {

        if (ficha != null) {
            if (exacto != null && exacto) {
                return ResponseEntity.ok().body(respaldoService.obtenerRespaldosPorFichaAnalistaExacta(ficha));
            }
            return ResponseEntity.ok().body(respaldoService.obtenerRespaldosPorFichaAnalista(ficha));
        }

        if (sistema_operativo != null) {
            return ResponseEntity.ok().body(respaldoService.obtenerRespaldosPorSistemaOperativo(sistema_operativo));
        }

        if (fmo_equipo != null) {
            return ResponseEntity.ok().body(respaldoService.obtenerRespaldosPorFmoEquipo(fmo_equipo));
        }

        if (nombre_completo != null) {
            return ResponseEntity.ok().body(respaldoService.obtenerRespaldosPorNombreAnalista(nombre_completo));
        }

        // CASO 0: si no se envia ni desde ni hasta, se devuelve todo el historial
        if (desde == null || desde.isEmpty() && hasta == null || hasta.isEmpty()) {
            return ResponseEntity.ok().body(respaldoService.obtenerTodosLosRespaldos());
        }

        // CASO 1: Si no envía 'desde', asumimos que quiere ver todo el historial
        if (desde == null || desde.isEmpty()) {
            desde = "1900-01-01 00:00:00";
        }

        // CASO 2: Si envía 'desde' pero no 'hasta', asumimos que 'hasta' es HOY AHORA
        // MISMO
        if (hasta == null || hasta.isEmpty()) {
            // Generamos la fecha actual en tu formato
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss");
            hasta = java.time.LocalDateTime.now().format(formatter);
        }

        // Ejecutamos la búsqueda por rango (ya sea el rango completo o desde X hasta
        // Hoy)
        return ResponseEntity.ok().body(respaldoService.obtenerRespaldosEntreFechas(desde, hasta));
    }

    @PostMapping("/respaldos/delete")
    public ResponseEntity<?> postMethodName(@RequestBody EliminarObjetos objetoAEliminar) {
        System.out.println("Eliminar respaldo con ID: " + objetoAEliminar.getId());
        try {
            return ResponseEntity.ok(respaldoService.ocultarRespaldo(objetoAEliminar.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el respaldo: " + e.getMessage());
        }
    }

    @PostMapping("/respaldos/editar-fmo")
    public ResponseEntity<?> editarFMO(@RequestBody EditarFMO editarFMO) {
        System.out.println("Editar FMO respaldo con ID: " + editarFMO.getId() + " nuevo FMO: " + editarFMO.getFmo_equipo());
        try {
            respaldoService.editarFmoEquipoRespaldo(editarFMO.getId(), editarFMO.getFmo_equipo());
            return ResponseEntity.ok(Map.of("ok", true));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error al editar el FMO: " + ex.getMessage());
        }
    }
}