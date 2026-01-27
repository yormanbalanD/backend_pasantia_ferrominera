package com.fmowinconf.dto.response;

import lombok.Data;

@Data
public class CrearAnalistaDTO {
    private String ficha;
    private String nombre_completo;
    private String password;
    private String permisos;
}
