package com.fmowinconf.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.config.auth.ContenidoJWT;
import com.fmowinconf.dto.request.CambiarVisible;
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALISTA')")
    @GetMapping("configuraciones")
    public ResponseEntity<?> getMethodName(
            @RequestParam(value = "desde", required = false) String desde,
            @RequestParam(value = "hasta", required = false) String hasta,
            @RequestParam(value = "ficha", required = false) String ficha,
            @RequestParam(value = "sistema_operativo", required = false) String sistema_operativo,
            @RequestParam(value = "fmo_equipo", required = false) String fmo_equipo,
            @RequestParam(value = "nombre_completo", required = false) String nombre_completo,
            @RequestParam(value = "exacto", required = false) Boolean exacto,
            Authentication authentication) { // <-- Inyectamos la autenticación

        Object resultado;

        // 1. Lógica de obtención de datos (tu lógica original)
        if (ficha != null) {
            System.out.println(exacto);

            if (exacto != null && exacto) {
                resultado = configuracionService.obtenerConfiguracionesPorFichaAnalistaExacta(ficha);
            } else {
                resultado = configuracionService.obtenerPorFichaAnalista(ficha);
            }
        } else if (sistema_operativo != null) {
            resultado = configuracionService.obtenerPorSistemaOperativo(sistema_operativo);
        } else if (fmo_equipo != null) {
            resultado = configuracionService.obtenerPorFmoEquipo(fmo_equipo);
        } else if (nombre_completo != null) {
            resultado = configuracionService.obtenerPorNombreAnalista(nombre_completo);
        } else {
            // Lógica de fechas
            boolean desdeEsNuloOVacio = (desde == null || desde.trim().isEmpty());
            boolean hastaEsNuloOVacio = (hasta == null || hasta.trim().isEmpty());

            if (desdeEsNuloOVacio && hastaEsNuloOVacio) {
                resultado = configuracionService.obtenerTodasLasConfiguraciones();
            } else {
                if (desdeEsNuloOVacio)
                    desde = "1900-01-01 00:00:00";
                if (hastaEsNuloOVacio) {
                    hasta = java.time.LocalDateTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                resultado = configuracionService.obtenerConfiguracionEntreFechas(desde, hasta);
            }
        }

        // 2. APLICAR FILTRO DE SEGURIDAD
        ContenidoJWT usuario = (ContenidoJWT) authentication.getPrincipal();

        if ("ANALISTA".equals(usuario.getPermisos()) && resultado instanceof List<?>) {
            List<?> lista = (List<?>) resultado;
            // Filtramos: Solo se mantienen si visible != 0
            // (Asegúrate de que la clase de los objetos en la lista tenga el método
            // getVisible())
            resultado = lista.stream()
                    .filter(item -> {
                        try {
                            // Usamos reflexión simple o casting si conoces la clase (ej. Configuracion)
                            // Aquí asumo que tus objetos tienen un método getVisible()
                            java.lang.reflect.Method getVisible = item.getClass().getMethod("getVisible");
                            int visible = (int) getVisible.invoke(item);
                            return visible != 0;
                        } catch (Exception e) {
                            return true; // Si no tiene el método, no filtramos para evitar errores
                        }
                    })
                    .toList();
        }

        return ResponseEntity.ok(resultado);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("configuraciones/delete")
    public ResponseEntity<?> eliminarConfiguracion(@RequestBody EliminarObjetos objetoAEliminar) {
        try {
            configuracionService.eliminarConfiguracion(objetoAEliminar.getId());

            return ResponseEntity.ok("Configuración eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar la configuración: " + e.getMessage());
        }
    }

    @PostMapping("configuraciones/visible")
    public ResponseEntity<?> cambiarVisibleConfiguracion(@RequestBody CambiarVisible objeto) {
        try {
            configuracionService.cambiarVisibleConfiguracion(objeto.getId(), objeto.getVisible());
            System.out.println("Eliminar configuración con ID: " + objeto.getId());
            return ResponseEntity.ok(Map.of("message", "Configuración eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar la configuración: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
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
