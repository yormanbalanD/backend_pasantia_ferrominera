package com.fmowinconf.config.auth;

import java.util.Date;

import lombok.Data;

@Data
public class ContenidoJWT {
    private long id;
    private String nombre_completo;
    private String ficha;
    private String permisos;
}
