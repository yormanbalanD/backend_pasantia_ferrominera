package com.fmowinconf.dto.response;

import lombok.Data;

@Data
public class ConfiguracionIPDTO {
    private long id;
    private String ip_address;
    private String subnet_mask;
    private String default_gateway;
    private String created_at;
}
