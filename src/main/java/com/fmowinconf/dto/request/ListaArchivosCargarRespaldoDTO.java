package com.fmowinconf.dto.request;

import java.util.List;

import com.fmowinconf.dto.response.ArchivoDTO;

import lombok.Data;

@Data
public class ListaArchivosCargarRespaldoDTO {
    private List<ArchivoDTO> archivos;
}
