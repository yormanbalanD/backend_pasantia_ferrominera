package com.fmowinconf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fmowinconf.models.Configuracion;
import com.fmowinconf.models.Respaldo;

@Repository
public interface RespaldoRepository extends JpaRepository<Respaldo, Long> {
        @Query("SELECT r FROM Respaldo r WHERE r.created_at BETWEEN :fechaInicio AND :fechaFin")
        List<Respaldo> buscarPorRangoDeFechas(@Param("fechaInicio") String fechaInicio,
                        @Param("fechaFin") String fechaFin);

        List<Respaldo> findByAnalistaId(Long analistaId);

        @Query("SELECT r FROM Respaldo r WHERE r.analista.id = :analistaId AND r.created_at BETWEEN :fechaInicio AND :fechaFin")
        List<Respaldo> buscarPorAnalistaYFechas(
                        @Param("analistaId") Long analistaId,
                        @Param("fechaInicio") String fechaInicio,
                        @Param("fechaFin") String fechaFin);

        // Nueva consulta que trae el respaldo y el analista en un solo viaje a la BD
        @Query("SELECT r FROM Respaldo r JOIN FETCH r.analista")
        List<Respaldo> findAllWithAnalista();

        // OPCIONAL: Si quieres que ignore mayúsculas y minúsculas (Más flexible)
        @Query("SELECT r FROM Respaldo r JOIN FETCH r.analista WHERE LOWER(r.fmo_equipo) LIKE LOWER(CONCAT(:equipo, '%'))")
        List<Respaldo> findByFmoEquipoStartingWithIgnoreCase(@Param("equipo") String equipo);

        // Búsqueda por Sistema Operativo (Comienza con...)
        @Query("SELECT r FROM Respaldo r JOIN FETCH r.analista WHERE r.sistema_operativo LIKE :so%")
        List<Respaldo> findBySistemaOperativoStartingWith(@Param("so") String so);

        /**
         * Busca respaldos por la ficha del analista (Comienza con...)
         */
        @Query("SELECT r FROM Respaldo r JOIN FETCH r.analista a " +
                        "WHERE LOWER(a.ficha) LIKE LOWER(CONCAT(:ficha, '%'))")
        List<Respaldo> findByAnalistaFichaStartingWithIgnoreCase(@Param("ficha") String ficha);

        /**
         * Busca configuraciones basadas en la ficha del analista.
         * Realiza un JOIN FETCH para obtener los datos del analista en la misma
         * consulta.
         */
        @Query("SELECT r FROM Respaldo r JOIN FETCH r.analista a WHERE a.ficha = :ficha")
        List<Respaldo> findByAnalistaFicha(@Param("ficha") String ficha);

        /**
         * Busca respaldos por el nombre del analista (Comienza con... e ignora
         * mayúsculas/minúsculas)
         */
        @Query("SELECT r FROM Respaldo r JOIN FETCH r.analista a " +
                        "WHERE LOWER(a.nombre_completo) LIKE LOWER(CONCAT(:nombre, '%'))")
        List<Respaldo> findByAnalistaNombreCompletoStartingWithIgnoreCase(@Param("nombre") String nombre);
}
