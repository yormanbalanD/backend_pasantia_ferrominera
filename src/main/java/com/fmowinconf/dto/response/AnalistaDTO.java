package com.fmowinconf.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AnalistaDTO {
    private long id;
    private String ficha;
    private String nombre_completo;
    private String password;
    private String permisos;
    private String created_at;
    private List<ConfiguracionDTO> configuraciones = new ArrayList<>();
    private List<RespaldoDTO> respaldos = new ArrayList<>();
}
