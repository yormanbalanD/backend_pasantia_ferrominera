package com.fmowinconf.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fmowinconf.models.Analista;
import com.fmowinconf.models.Archivo;

import lombok.Data;

@Data
public class RespaldoDTO {
    private long id;
    private Analista analista;
    private String fmo_equipo;
    private String sistema_operativo;
    private String created_at;
    private List<ArchivoDTO> archivos = new ArrayList<>();
}
