package com.fmowinconf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmowinconf.repository.AnalistaRepository;
import com.fmowinconf.dto.response.AnalistaDTO;
import com.fmowinconf.models.Analista;

import java.util.List;
@Service
public class AnalistaServiceImpl implements IAnalistaService {

    @Autowired  
    private AnalistaRepository analistaRepository;

    public List<Analista> getAllAnalistas(){
        return analistaRepository.findAll();
    }

    public Analista crearAnalista(AnalistaDTO analista){
        Analista newAnalista = new Analista();
        newAnalista.setFicha(analista.getFicha());
        newAnalista.setNombre_completo(analista.getNombre_completo());
        newAnalista.setPassword(analista.getPassword());
        newAnalista.setPermisos(analista.getPermisos());

        return analistaRepository.save(newAnalista);
    }    
}
