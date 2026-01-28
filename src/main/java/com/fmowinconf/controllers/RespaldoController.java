package com.fmowinconf.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.config.auth.ContenidoJWT;
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALISTA')")
    @GetMapping("/respaldos")
    public ResponseEntity<?> obtenerTodosLosRespaldos(
            @RequestParam(value = "desde", required = false) String desde,
            @RequestParam(value = "hasta", required = false) String hasta,
            @RequestParam(value = "ficha", required = false) String ficha,
            @RequestParam(value = "sistema_operativo", required = false) String sistema_operativo,
            @RequestParam(value = "fmo_equipo", required = false) String fmo_equipo,
            @RequestParam(value = "nombre_completo", required = false) String nombre_completo,
            @RequestParam(value = "exacto", required = false) Boolean exacto,
            Authentication authentication) {

        Object resultado;

        // 1. Lógica de selección de datos
        if (ficha != null) {
            if (exacto != null && exacto) {
                resultado = respaldoService.obtenerRespaldosPorFichaAnalistaExacta(ficha);
            } else {
                resultado = respaldoService.obtenerRespaldosPorFichaAnalista(ficha);
            }
        } else if (sistema_operativo != null) {
            resultado = respaldoService.obtenerRespaldosPorSistemaOperativo(sistema_operativo);
        } else if (fmo_equipo != null) {
            resultado = respaldoService.obtenerRespaldosPorFmoEquipo(fmo_equipo);
        } else if (nombre_completo != null) {
            resultado = respaldoService.obtenerRespaldosPorNombreAnalista(nombre_completo);
        } else {
            // Lógica de fechas
            if ((desde == null || desde.isEmpty()) && (hasta == null || hasta.isEmpty())) {
                resultado = respaldoService.obtenerTodosLosRespaldos();
            } else {
                if (desde == null || desde.isEmpty())
                    desde = "1900-01-01 00:00:00";
                if (hasta == null || hasta.isEmpty()) {
                    hasta = java.time.LocalDateTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                resultado = respaldoService.obtenerRespaldosEntreFechas(desde, hasta);
            }
        }

        // 2. Aplicación de la regla de negocio: ANALISTA solo ve visibles != 0
        ContenidoJWT usuario = (ContenidoJWT) authentication.getPrincipal();

        if ("ANALISTA".equals(usuario.getPermisos()) && resultado instanceof List<?>) {
            // Filtramos la lista dinámicamente
            resultado = ((List<?>) resultado).stream()
                    .filter(item -> {
                        // Si tus clases de modelo heredan de una base o son iguales,
                        // puedes castear a una interfaz o clase específica aquí.
                        // Usaremos una validación segura por si el objeto no tiene el campo.
                        try {
                            return (int) item.getClass().getMethod("getVisible").invoke(item) != 0;
                        } catch (Exception e) {
                            return true; // En caso de error, mostramos el registro por defecto
                        }
                    })
                    .toList();
        }

        return ResponseEntity.ok(resultado);
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/respaldos/editar-fmo")
    public ResponseEntity<?> editarFMO(@RequestBody EditarFMO editarFMO) {
        System.out.println(
                "Editar FMO respaldo con ID: " + editarFMO.getId() + " nuevo FMO: " + editarFMO.getFmo_equipo());
        try {
            respaldoService.editarFmoEquipoRespaldo(editarFMO.getId(), editarFMO.getFmo_equipo());
            return ResponseEntity.ok(Map.of("ok", true));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error al editar el FMO: " + ex.getMessage());
        }
    }
}