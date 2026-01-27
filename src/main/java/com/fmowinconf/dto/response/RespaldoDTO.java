package com.fmowinconf.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fmowinconf.models.Analista;

import lombok.Data;



@Data
public class RespaldoDTO {
    private long id;
    private Analista analista;
    private String fmo_equipo;
    private String sistema_operativo;
    private String created_at;
    private String tiempo_end;
    private String tiempo_start;
    private String tipo;
    private int completado_con_exito;
    private int cantidad_de_archivos;
    private List<ArchivoDTO> archivos = new ArrayList<>();
}
