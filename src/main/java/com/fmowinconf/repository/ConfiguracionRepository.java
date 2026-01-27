package com.fmowinconf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fmowinconf.models.Configuracion;
import com.fmowinconf.models.Respaldo;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
        @Query("SELECT c FROM Configuracion c JOIN FETCH c.analista WHERE c.created_at BETWEEN :fechaInicio AND :fechaFin")
        List<Configuracion> buscarPorRangoDeFechas(@Param("fechaInicio") String fechaInicio,
                        @Param("fechaFin") String fechaFin);

        @Query("SELECT c FROM Configuracion c JOIN FETCH c.analista WHERE c.analista.id = :analistaId")
        List<Configuracion> findByAnalistaId(@Param("analistaId") Long analistaId);

        @Query("SELECT c FROM Configuracion c JOIN FETCH c.analista WHERE c.analista.id = :analistaId AND c.created_at BETWEEN :fechaInicio AND :fechaFin")
        List<Configuracion> buscarPorAnalistaYFechas(
                        @Param("analistaId") Long analistaId,
                        @Param("fechaInicio") String fechaInicio,
                        @Param("fechaFin") String fechaFin);

        // Nueva consulta que trae la configuración y el analista en un solo viaje a la
        // BD
        @Query("SELECT c FROM Configuracion c JOIN FETCH c.analista")
        List<Configuracion> findAllWithAnalista();

        // OPCIONAL: Si quieres que ignore mayúsculas y minúsculas (Más flexible)
        @Query("SELECT c FROM Configuracion c JOIN FETCH c.analista WHERE LOWER(c.fmo_equipo) LIKE LOWER(CONCAT(:equipo, '%'))")
        List<Configuracion> findByFmoEquipoStartingWithIgnoreCase(@Param("equipo") String equipo);

        // Búsqueda por Sistema Operativo (Comienza con...)
        @Query("SELECT c FROM Configuracion c JOIN FETCH c.analista WHERE c.sistema_operativo LIKE :so%")
        List<Configuracion> findBySistemaOperativoStartingWith(@Param("so") String so);

        /**
         * Busca configuraciones por la ficha del analista (Comienza con...)
         */
        @Query("SELECT c FROM Configuracion c JOIN FETCH c.analista a " +
                        "WHERE LOWER(a.ficha) LIKE LOWER(CONCAT(:ficha, '%'))")
        List<Configuracion> findByAnalistaFichaStartingWithIgnoreCase(@Param("ficha") String ficha);

        /**
         * Busca configuraciones basadas en la ficha del analista.
         * Realiza un JOIN FETCH para obtener los datos del analista en la misma
         * consulta.
         */
        @Query("SELECT c FROM Configuracion c JOIN FETCH c.analista a WHERE a.ficha = :ficha")
        List<Configuracion> findByAnalistaFicha(@Param("ficha") String ficha);

        /**
         * Busca configuraciones por el nombre del analista (Comienza con... e ignora
         * mayúsculas/minúsculas)
         */
        @Query("SELECT c FROM Configuracion c JOIN FETCH c.analista a " +
                        "WHERE LOWER(a.nombre_completo) LIKE LOWER(CONCAT(:nombre, '%'))")
        List<Configuracion> findByAnalistaNombreCompletoStartingWithIgnoreCase(@Param("nombre") String nombre);
}
