package com.fmowinconf.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmowinconf.dto.response.ArchivoDTO;
import com.fmowinconf.dto.response.RespaldoDTO;
import com.fmowinconf.models.Archivo;
import com.fmowinconf.models.Respaldo;
import com.fmowinconf.repository.RespaldoRepository;

@Service
public class RespaldoServiceImpl {
    @Autowired
    private RespaldoRepository respaldoRepository;

    public Respaldo crearRespaldo(RespaldoDTO respaldoDTO) {
        System.out.println(respaldoDTO.toString());
        Respaldo respaldo = new Respaldo();
        respaldo.setAnalista(respaldoDTO.getAnalista());
        respaldo.setFmo_equipo(respaldoDTO.getFmo_equipo());
        respaldo.setSistema_operativo(respaldoDTO.getSistema_operativo());
        respaldo.setTipo(respaldoDTO.getTipo());
        respaldo.setCompletado_con_exito(2);

        respaldo.setTiempo_end(respaldoDTO.getTiempo_end());
        respaldo.setTiempo_start(respaldoDTO.getTiempo_start());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String created_at = LocalDateTime.now().format(formatter);
        respaldo.setCreated_at(created_at);

        // Verificamos si vienen archivos en el DTO
        if (respaldoDTO.getArchivos() != null &&
                !respaldoDTO.getArchivos().isEmpty()) {

            // Convertimos la lista de DTOs a Entidades usando un método auxiliar
            // Pasamos 'respaldo' para asignar la FK id_respaldo
            // Pasamos 'null' como padre porque estos son los archivos raiz
            List<Archivo> archivosEntidad = respaldoDTO.getArchivos().stream()
                    .map(dto -> mapearArchivoRecursivo(dto, respaldo, null))
                    .toList(); // O .collect(Collectors.toList()) si usas Java < 16

            respaldo.setArchivos(archivosEntidad);
        }

        return respaldoRepository.save(respaldo);
    }

    /**
     * Método recursivo para convertir DTO a Entidad y establecer relaciones
     * bidireccionales.
     * 
     * @param dto      El DTO del archivo/carpeta actual.
     * @param respaldo La entidad padre Respaldo (para la FK id_respaldo).
     * @param padre    La entidad Archivo padre (para la FK id_padre), null si es
     *                 raíz.
     * @return La entidad Archivo construida con toda su jerarquía.
     */
    private Archivo mapearArchivoRecursivo(ArchivoDTO dto, Respaldo respaldo, Archivo padre) {
        Archivo archivo = new Archivo();

        // 1. Mapeo de campos simples
        archivo.setNombre_archivo(dto.getNombre_archivo());
        archivo.setEs_carpeta(dto.isEs_carpeta() ? 1 : 0);
        archivo.setRuta(dto.getRuta());
        archivo.setExtension(dto.getExtension());
        archivo.setTamaño(dto.getTamaño());

        // 2. Establecer relaciones padre (hacia arriba)
        archivo.setRespaldo(respaldo); // FK id_respaldo
        archivo.setPadre(padre); // FK id_padre

        // 3. Procesar Hijos recursivamente (hacia abajo)
        if (dto.getHijos() != null && !dto.getHijos().isEmpty()) {
            List<Archivo> hijosEntidades = new ArrayList<>();
            for (ArchivoDTO hijoDto : dto.getHijos()) {
                // LLAMADA RECURSIVA:
                // Pasamos el 'archivo' actual como el 'padre' del siguiente nivel
                hijosEntidades.add(mapearArchivoRecursivo(hijoDto, respaldo, archivo));
            }
            archivo.setHijos(hijosEntidades);
        }

        return archivo;
    }

    public Respaldo finalizarRespaldo(Respaldo respaldo, int estadoFinalizacion) {
        respaldo.setCompletado_con_exito(estadoFinalizacion);
        return respaldoRepository.save(respaldo);
    }

    public List<Respaldo> obtenerTodosLosRespaldos() {
        return respaldoRepository.findAllWithAnalista();
    }

    public List<Respaldo> obtenerRespaldosEntreFechas(String fechaInicio, String fechaFin) {
        // Asumimos que el input es solo "yyyy-MM-dd", así que completamos la hora
        // para asegurarnos de que la búsqueda incluya todo el rango.

        // Inicio del día X
        String inicioCompleto = fechaInicio.contains(":") ? fechaInicio : fechaInicio + " 00:00:00";

        // Final del día Y
        String finCompleto = fechaFin.contains(":") ? fechaFin : fechaFin + " 23:59:59";

        return respaldoRepository.buscarPorRangoDeFechas(inicioCompleto, finCompleto);
    }

    public List<Respaldo> obtenerRespaldosPorFmoEquipo(String fmo_equipo) {
        return respaldoRepository.findByFmoEquipoStartingWithIgnoreCase(fmo_equipo);
    }

    public List<Respaldo> obtenerRespaldosPorFichaAnalista(String ficha) {
        return respaldoRepository.findByAnalistaFichaStartingWithIgnoreCase(ficha);
    }

    public List<Respaldo> obtenerRespaldosPorNombreAnalista(String nombre) {
        return respaldoRepository.findByAnalistaNombreCompletoStartingWithIgnoreCase(nombre);
    }

    public List<Respaldo> obtenerRespaldosPorSistemaOperativo(String sistema_operativo) {
        return respaldoRepository.findBySistemaOperativoStartingWith(sistema_operativo);
    }

    public List<Respaldo> obtenerRespaldosPorFichaAnalistaExacta(String ficha) {
        return respaldoRepository.findByAnalistaFicha(ficha);
    }

    public List<Respaldo> obtenerRespaldosPorAnalista(Long id_analista) {
        return respaldoRepository.findByAnalistaId(id_analista);
    }

    public Respaldo ocultarRespaldo(Long id) {
        try {
            Respaldo existingRespaldo = respaldoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Respaldo no encontrado con id: " + id));

            existingRespaldo.setVisible(0);

            return respaldoRepository.save(existingRespaldo);
        } catch (Exception e) {
            throw new RuntimeException("Error al ocultar el respaldo: " + e.getMessage());
        }
    }

    public Respaldo editarFmoEquipoRespaldo(Long id, String fmo_equipo) {
        Optional<Respaldo> data = respaldoRepository.findById(id);

        if(data.isEmpty()){
            throw new RuntimeException("Respaldo no encontrado con id: " + id);
        } else {
            Respaldo existingRespaldo = data.get();
            existingRespaldo.setFmo_equipo(fmo_equipo);
            return respaldoRepository.save(existingRespaldo);
        }
    }
}