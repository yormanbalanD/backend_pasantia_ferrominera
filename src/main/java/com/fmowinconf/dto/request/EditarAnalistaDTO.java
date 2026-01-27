package com.fmowinconf.dto.request;

import lombok.Data;


@Data
public class EditarAnalistaDTO {
    String ficha;
    String nombre_completo;
    String password;
    String permisos;
}
