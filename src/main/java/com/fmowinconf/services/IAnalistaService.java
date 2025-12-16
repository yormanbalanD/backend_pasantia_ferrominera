package com.fmowinconf.services;

import java.util.List;

import com.fmowinconf.dto.response.AnalistaDTO;
import com.fmowinconf.models.Analista;

public interface IAnalistaService {
    public List<Analista> getAllAnalistas();
     Analista crearAnalista(AnalistaDTO analista);
}
