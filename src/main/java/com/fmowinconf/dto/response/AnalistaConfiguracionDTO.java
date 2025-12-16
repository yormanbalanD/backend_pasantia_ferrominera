package com.fmowinconf.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fmowinconf.models.Configuracion;
import com.fmowinconf.models.Respaldo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

@Data
public class AnalistaConfiguracionDTO {
    private long id;
    private String ficha;
    private String nombre_completo;
    private String password;
    private String created_at;

    private List<ConfiguracionDTO> configuraciones = new ArrayList<>();
    private List<RespaldoDTO> respaldos = new ArrayList<>();
}
