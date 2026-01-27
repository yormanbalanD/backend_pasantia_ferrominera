package com.fmowinconf.dto.response;

import lombok.Data;

@Data
public class LoginDTO {
    private Boolean ok;
    private String token;
    private String nombre_completo;
    private String ficha;
    private String permisos;
    private long id;
    private String error;

    public LoginDTO(Boolean ok, String token) {
        this.ok = ok;
        this.token = token;
    }

    public LoginDTO(Boolean ok, String token, String error) {
        this.ok = ok;
        this.token = token;
        this.error = error;
    }

    public LoginDTO() {
    }
}
