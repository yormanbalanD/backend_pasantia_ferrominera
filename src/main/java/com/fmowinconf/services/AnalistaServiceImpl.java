package com.fmowinconf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmowinconf.repository.AnalistaRepository;
import com.fmowinconf.dto.request.EditarAnalistaDTO;
import com.fmowinconf.dto.response.CrearAnalistaDTO;
import com.fmowinconf.models.Analista;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AnalistaServiceImpl implements IAnalistaService {

    @Autowired
    private AnalistaRepository analistaRepository;

    public List<Analista> getAllAnalistas() {
        return analistaRepository.findAll();
    }

    public Analista crearAnalista(CrearAnalistaDTO analista) {
        Analista newAnalista = new Analista();
        newAnalista.setFicha(analista.getFicha());
        newAnalista.setNombre_completo(analista.getNombre_completo());
        newAnalista.setPassword(analista.getPassword());
        newAnalista.setPermisos(analista.getPermisos());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String created_at = LocalDateTime.now().format(formatter);

        newAnalista.setCreated_at(created_at);

        return analistaRepository.save(newAnalista);
    }

    public Analista editarAnalista(Long id, EditarAnalistaDTO analistaDTO) {
        try {
            Analista existingAnalista = analistaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Analista no encontrado con id: " + id));

            if (analistaDTO.getFicha() != null) {
                existingAnalista.setFicha(analistaDTO.getFicha());
            }

            if (analistaDTO.getNombre_completo() != null) {
                existingAnalista.setNombre_completo(analistaDTO.getNombre_completo());
            }

            if (analistaDTO.getPassword() != null) {
                existingAnalista.setPassword(analistaDTO.getPassword());
            }

            if (analistaDTO.getPermisos() != null) {
                existingAnalista.setPermisos(analistaDTO.getPermisos());
            }

            return analistaRepository.save(existingAnalista);
        } catch (Exception e) {
            throw new RuntimeException("Error al editar el analista: " + e.getMessage());
        }
    }

    public Analista ocultarAnalista(Long id) {
        try {
            Analista existingAnalista = analistaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Analista no encontrado con id: " + id));

            existingAnalista.setVisible(0);

            return analistaRepository.save(existingAnalista);
        } catch (Exception e) {
            throw new RuntimeException("Error al ocultar el analista: " + e.getMessage());
        }
    }
}
