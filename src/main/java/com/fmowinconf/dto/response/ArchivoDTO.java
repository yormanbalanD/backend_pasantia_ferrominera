package com.fmowinconf.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class ArchivoDTO {
    private long id;
    private RespaldoDTO respaldo;
    private ArchivoDTO padre;

    private List<ArchivoDTO> hijos = new ArrayList<>();
    private String nombre_archivo;
    private int es_carpeta;
    private String ruta;
    private String extension;
    private String tamaño;
    private String ruta_destino;
}
