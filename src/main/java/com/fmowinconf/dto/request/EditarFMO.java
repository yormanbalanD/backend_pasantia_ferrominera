package com.fmowinconf.dto.request;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.Data;

@Data
public class EditarFMO {
    private String fmo_equipo;
    private Long id;
}
