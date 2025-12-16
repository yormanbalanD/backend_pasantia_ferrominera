package com.fmowinconf.dto.response;

import lombok.Data;

@Data
public class ConfiguracionImpresoraDTO {
    private long id;
    private String modelo;
    private String ip_address;
    private String created_at;
}
