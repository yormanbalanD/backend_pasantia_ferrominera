package com.fmowinconf.dto.response;

import com.fmowinconf.models.Analista;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ConfiguracionDTO {
    private long id;
    private Analista analista;

    private String fmo_equipo;
    private int crear_usuario;
    private int mozilla_firefox;
    private int mozilla_thunderbird;
    private int hostname_dominio;
    private int configurar_impresora;
    private int configurar_escaner;
    private int usuario_administrador;
    private int desactivar_usuario_actual;

    private String sistema_operativo;

    private int configurar_ip;

    private String created_at;

    private ConfiguracionIPDTO ipConfig;
    private ConfiguracionImpresoraDTO impresora;
}
