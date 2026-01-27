package com.fmowinconf.dto.request;

import java.util.List;

import com.fmowinconf.dto.response.ArchivoDTO;

import lombok.Data;

@Data
public class ListaArchivosCrearRespaldoDTO {
    private long id_analista;
    private String token;
    private List<ArchivoDTO> archivos;
}
