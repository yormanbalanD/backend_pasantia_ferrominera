package com.fmowinconf.services;

import java.util.List;

import com.fmowinconf.dto.request.EditarAnalistaDTO;
import com.fmowinconf.dto.response.CrearAnalistaDTO;
import com.fmowinconf.models.Analista;

public interface IAnalistaService {
    public List<Analista> getAllAnalistas();

    Analista crearAnalista(CrearAnalistaDTO analista);

    public Analista editarAnalista(Long id, EditarAnalistaDTO analistaDTO);

    public Analista ocultarAnalista(Long id);
}
