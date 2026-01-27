package com.fmowinconf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmowinconf.dto.request.ListaArchivosCrearRespaldoDTO;
import com.fmowinconf.repository.ArchivoRepository;

@Service
public class ArchivoServiceImpl {
    @Autowired
    ArchivoRepository archivoRepository;   

    public void crearListaArchivosCrearRespaldo(ListaArchivosCrearRespaldoDTO arbolDeArchivos){
        
    }
}
